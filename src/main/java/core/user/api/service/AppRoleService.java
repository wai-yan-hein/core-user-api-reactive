package core.user.api.service;

import core.user.api.model.AppRole;
import core.user.api.model.RoleProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppRoleService {
    private final R2dbcEntityTemplate template;
    private final SeqService seqService;

    public Flux<AppRole> findAll() {
        return template.select(AppRole.class).all();
    }

    public Mono<AppRole> saveOrUpdate(AppRole role) {
        String roleCode = role.getRoleCode();
        role.setUpdatedDate(LocalDateTime.now());
        if (roleCode == null) {
            return seqService.getNextCode("Role", 5).flatMap(seqNo -> {
                role.setRoleCode(seqNo);
                return template.insert(role);
            });
        }
        return template.update(role);
    }

    public Mono<AppRole> findById(String roleCode) {
        return template.select(AppRole.class).matching(Query.query(Criteria.where("role_code").is(roleCode))).one();
    }

    public Flux<AppRole> getRoleByDate(String updatedDate) {
        return template.select(AppRole.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all();
    }
}
