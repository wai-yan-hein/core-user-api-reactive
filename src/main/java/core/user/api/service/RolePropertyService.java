package core.user.api.service;

import core.user.api.dto.RolePropertyDto;
import core.user.api.model.RoleProperty;
import io.r2dbc.spi.Parameters;
import io.r2dbc.spi.R2dbcType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RolePropertyService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;

    public Flux<RoleProperty> getSystemPropertyByDate(String updatedDate) {
        return template.select(RoleProperty.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all();
    }

    public Flux<RolePropertyDto> getRoleProperty(String roleCode, String compCode) {
        return template.select(RoleProperty.class)
                .matching(Query.query(Criteria.where("role_code")
                        .is(roleCode)
                        .and("comp_code").
                        is(compCode)))
                .all().map(RoleProperty::buildDto);
    }

    public Mono<RolePropertyDto> saveOrUpdate(RolePropertyDto dto) {
        RoleProperty role = dto.toEntity();
        String roleCode = role.getRoleCode();
        String compCode = role.getCompCode();
        String propKey = role.getPropKey();
        return findById(roleCode, compCode, propKey)
                .flatMap(exist -> update(role))
                .switchIfEmpty(template.insert(role).map(RoleProperty::buildDto));
    }


    private Mono<RolePropertyDto> update(RoleProperty role) {
        String sql = """
                update role_property
                set prop_value = :propValue,remark = :remark,updated_date = :updateDate
                where role_code =:roleCode and comp_code =:compCode and prop_key =:propKey
                """;
        return databaseClient.sql(sql)
                .bind("roleCode", role.getRoleCode())
                .bind("compCode", role.getCompCode())
                .bind("propKey", role.getPropKey())
                .bind("propValue", Parameters.in(R2dbcType.VARCHAR, role.getPropValue()))
                .bind("remark", Parameters.in(R2dbcType.VARCHAR, role.getRemark()))
                .bind("updateDate", LocalDateTime.now())
                .fetch().rowsUpdated().thenReturn(role.buildDto());
    }

    public Mono<RolePropertyDto> findById(String roleCode, String compCode, String propKey) {
        return template.select(RoleProperty.class)
                .matching(Query.query(Criteria.where("role_code")
                        .is(roleCode)
                        .and("comp_code").is(compCode)
                        .and("prop_key").is(propKey)))
                .one().map(RoleProperty::buildDto);
    }

    public Mono<Boolean> delete(RoleProperty rp) {
        return template.delete(RoleProperty.class)
                .matching(Query.query(Criteria.where("role_code")
                        .is(rp.getRoleCode())
                        .and("comp_code").is(rp.getCompCode())
                        .and("prop_key").is(rp.getPropValue())))
                .all()
                .thenReturn(true);
    }

}
