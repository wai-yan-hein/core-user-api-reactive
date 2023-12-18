package core.user.api.service;

import core.user.api.dto.PrivilegeMenuDto;
import core.user.api.model.PrivilegeMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PrivilegeMenuService {
    private final R2dbcEntityTemplate template;

    public Flux<PrivilegeMenuDto> findAll(String compCode) {
        return template.select(PrivilegeMenu.class)
                .matching(Query.query(Criteria.where("comp_code")
                        .is(compCode)))
                .all()
                .map(PrivilegeMenu::buildDto);
    }

    public Mono<PrivilegeMenuDto> saveOrUpdate(PrivilegeMenuDto dto) {
        PrivilegeMenu role = dto.toEntity();
        String roleCode = role.getRoleCode();
        String compCode = role.getCompCode();
        return findById(roleCode, compCode)
                .flatMap(exist -> template.update(role)).map(PrivilegeMenu::buildDto)
                .switchIfEmpty(template.update(role).map(PrivilegeMenu::buildDto));
    }

    public Mono<PrivilegeMenuDto> findById(String projectNo, String compCode) {
        return template.select(PrivilegeMenu.class)
                .matching(Query.query(Criteria.where("project_no")
                        .is(projectNo).and("comp_code")
                        .is(compCode)))
                .one().map(PrivilegeMenu::buildDto);
    }

    public Flux<PrivilegeMenuDto> getProjectByDate(String updatedDate) {
        return template.select(PrivilegeMenu.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(PrivilegeMenu::buildDto);
    }

}
