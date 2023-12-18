package core.user.api.service;

import core.user.api.common.Util1;
import core.user.api.dto.MenuDto;
import core.user.api.dto.MenuKey;
import core.user.api.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;
    private final SeqService seqService;

    public Flux<MenuDto> findAll(String compCode) {
        return template.select(Menu.class)
                .matching(Query.query(Criteria.where("comp_code")
                        .is(compCode)))
                .all().map(Menu::buildDto);
    }

    public Mono<MenuDto> saveOrUpdate(MenuDto dto) {
        Menu menu = dto.toEntity();
        String menuCode = menu.getMenuCode();
        menu.setUpdatedDate(LocalDateTime.now());
        if (Util1.isNullOrEmpty(menuCode)) {
            return seqService.getNextCode("Menu", 5).flatMap(seqNo -> {
                menu.setMenuCode(seqNo);
                return template.insert(menu).map(Menu::buildDto);
            });
        }
        return template.update(menu).map(Menu::buildDto);
    }

    public Mono<MenuDto> findById(String menuCode, String compCode) {
        return template.select(Menu.class)
                .matching(Query.query(Criteria.where("menu_code")
                        .is(menuCode).and("comp_code").is(compCode))).one().map(Menu::buildDto);
    }

    public Flux<MenuDto> getMenuByDate(String updatedDate) {
        return template.select(Menu.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(Menu::buildDto);
    }

    public Flux<MenuDto> getPrivilegeMenuTree(String roleCode, String compCode, boolean privilege) {
        return getPrivilegeMenu(roleCode, "#", compCode, privilege)
                .collectList()
                .flatMapMany(list -> Flux.fromIterable(list)
                        .flatMapSequential(this::createPrivilegeChild));
    }

    public Flux<MenuDto> getMenuTree(String parentId, String compCode) {
        return searchMenu(parentId, compCode)
                .collectList()
                .flatMapMany(list -> Flux.fromIterable(list)
                        .flatMapSequential(this::createChild));
    }

    private Mono<MenuDto> createChild(MenuDto dto) {
        String parentId = dto.getKey().getMenuCode();
        String compCode = dto.getKey().getCompCode();// assuming parentId is actually fileId
        return searchMenu(parentId, compCode)
                .collectList()
                .flatMap(children -> {
                    dto.setChild(children);
                    return Flux.fromIterable(children)
                            .flatMapSequential(this::createChild)
                            .then(Mono.just(dto));
                });
    }

    private Mono<MenuDto> createPrivilegeChild(MenuDto dto) {
        String parentId = dto.getKey().getMenuCode();
        String compCode = dto.getKey().getCompCode();
        String roleCode = dto.getRoleCode();
        boolean privilege = dto.getAllow();
        return getPrivilegeMenu(roleCode, parentId, compCode, privilege)
                .collectList()
                .flatMap(children -> {
                    dto.setChild(children);
                    return Flux.fromIterable(children)
                            .flatMapSequential(this::createChild)
                            .then(Mono.just(dto));
                });
    }

    public Flux<MenuDto> searchMenu(String parentId, String compCode) {
        Criteria criteria = Criteria.where("parent_menu_code")
                .is(parentId)
                .and("comp_code").is(compCode);
        Sort sort = Sort.by(Sort.Order.desc("order_by"));
        return template.select(Menu.class)
                .matching(query(criteria).sort(sort))
                .all()
                .map(Menu::buildDto);
    }

    public Mono<Boolean> deleteMenu(MenuDto menu) {
        return template.delete(Menu.class)
                .matching(Query.query(Criteria.where("menu_code").is(menu.getKey().getMenuCode())
                        .and("comp_code")
                        .is(menu.getKey().getCompCode()))).all().thenReturn(true);
    }

    public Flux<MenuDto> getMenuDynamic(String compCode) {
        String sql = """
                select *
                from menu
                where (menu_class ='AllCash' or menu_class='DayBook')
                and (account is null or account ='')
                and comp_code =:compCode
                """;
        //menu_code, comp_code, user_code, menu_class, menu_name, menu_name_mm, menu_url, parent_menu_code,
        // menu_type, account, order_by, updated_date
        return databaseClient.sql(sql)
                .bind("compCode", compCode)
                .map((row) -> MenuDto.builder()
                        .key(MenuKey.builder()
                                .menuCode(row.get("menu_code", String.class))
                                .compCode(row.get("comp_code", String.class))
                                .build())
                        .userCode(row.get("user_code", String.class))
                        .menuClass(row.get("menu_class", String.class))
                        .menuName(row.get("menu_name", String.class))
                        .menuUrl(row.get("menu_url", String.class))
                        .parentMenuCode(row.get("parent_menu_code", String.class))
                        .menuType(row.get("menu_type", String.class))
                        .account(row.get("account", String.class))
                        .orderBy(row.get("order_by", Integer.class))
                        .updatedDate(row.get("updated_date", LocalDateTime.class))
                        .build())
                .all();
    }

    public Flux<MenuDto> getReport(String roleCode, String menuClass, String compCode) {
        menuClass = Util1.isNull(menuClass, "-");
        String sql = """
                select o.menu_code,o.role_code,o.comp_code,o.allow,
                o.menu_name,o.menu_url,o.menu_type,o.menu_class,
                o.account,o.parent_menu_code,o.order_by
                from(
                select p.menu_code,p.role_code,p.comp_code,p.allow,
                m.menu_name,m.menu_url,m.menu_type,m.menu_class,
                m.account,m.parent_menu_code,m.order_by
                from privilege_menu p
                join menu m on p.menu_code=m.menu_code
                and p.comp_code=m.comp_code
                )o
                where o.role_code=:roleCode and o.menu_type='Report'
                and o.comp_code=:compCode and(o.menu_class=:menuClass or'-'=:menuClass)
                and allow = true
                order by o.order_by""";
        //menu_code, comp_code, user_code, menu_class, menu_name, menu_name_mm, menu_url, parent_menu_code,
        // menu_type, account, order_by, updated_date
        return databaseClient.sql(sql)
                .bind("compCode", compCode)
                .bind("menuClass", menuClass)
                .bind("roleCode", roleCode)
                .map((row) -> MenuDto.builder()
                        .key(MenuKey.builder()
                                .menuCode(row.get("menu_code", String.class))
                                .compCode(row.get("comp_code", String.class))
                                .build())
                        .roleCode(row.get("role_code", String.class))
                        .allow(row.get("allow", Boolean.class))
                        .menuClass(row.get("menu_class", String.class))
                        .menuName(row.get("menu_name", String.class))
                        .menuUrl(row.get("menu_url", String.class))
                        .parentMenuCode(row.get("parent_menu_code", String.class))
                        .menuType(row.get("menu_type", String.class))
                        .account(row.get("account", String.class))
                        .orderBy(row.get("order_by", Integer.class))
                        .build())
                .all();
    }

    public Flux<MenuDto> getPrivilegeMenu(String roleCode, String parentCode,
                                          String compCode,
                                          boolean privilege) {
        String sql = """
                select o.menu_code,o.role_code,o.comp_code,o.allow,
                o.menu_name,o.menu_url,o.menu_type,o.menu_class,
                o.account,o.parent_menu_code,o.order_by,o.menu_version
                from(
                select p.menu_code,p.role_code,p.comp_code,p.allow,
                m.menu_name,m.menu_url,m.menu_type,m.menu_class,
                m.account,m.parent_menu_code,m.order_by,m.menu_version
                from privilege_menu p
                join menu m on p.menu_code=m.menu_code
                and p.comp_code=m.comp_code
                )o
                where o.role_code=:roleCode
                and o.comp_code=:compCode
                and o.parent_menu_code=:parentMenuCode
                and o.menu_type='Menu'
                and (allow =:privilege  or false = :privilege)
                order by o.order_by""";
        //menu_code, comp_code, user_code, menu_class, menu_name, menu_name_mm, menu_url, parent_menu_code,
        // menu_type, account, order_by, updated_date
        return databaseClient.sql(sql)
                .bind("roleCode", roleCode)
                .bind("compCode", compCode)
                .bind("parentMenuCode", parentCode)
                .bind("privilege", privilege)
                .map((row) -> MenuDto.builder()
                        .key(MenuKey.builder()
                                .menuCode(row.get("menu_code", String.class))
                                .compCode(row.get("comp_code", String.class))
                                .build())
                        .roleCode(row.get("role_code", String.class))
                        .allow(row.get("allow", Boolean.class))
                        .menuClass(row.get("menu_class", String.class))
                        .menuName(row.get("menu_name", String.class))
                        .menuUrl(row.get("menu_url", String.class))
                        .parentMenuCode(row.get("parent_menu_code", String.class))
                        .menuType(row.get("menu_type", String.class))
                        .account(row.get("account", String.class))
                        .orderBy(row.get("order_by", Integer.class))
                        .build())
                .all();
    }
}
