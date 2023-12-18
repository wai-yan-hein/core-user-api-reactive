package core.user.api.service;

import core.user.api.common.Util1;
import core.user.api.dto.SystemPropertyDto;
import core.user.api.model.SystemProperty;
import io.r2dbc.spi.Parameters;
import io.r2dbc.spi.R2dbcType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemPropertyService {
    private final R2dbcEntityTemplate template;
    private final RolePropertyService rolePropertyService;
    private final MachinePropertyService machinePropertyService;
    private final DatabaseClient databaseClient;

    public Mono<SystemPropertyDto> saveOrUpdate(SystemPropertyDto dto) {
        SystemProperty property = dto.toEntity();
        String propKey = property.getPropKey();
        String compCode = property.getCompCode();
        return findById(propKey, compCode)
                .flatMap(exist -> update(property))
                .switchIfEmpty(template.insert(property).map(SystemProperty::buildDto));
    }
    private Mono<SystemPropertyDto> update(SystemProperty role) {
        String sql = """
                update system_property
                set prop_value = :propValue,remark = :remark,updated_date = :updateDate
                where comp_code =:compCode and prop_key =:propKey
                """;
        return databaseClient.sql(sql)
                .bind("compCode", role.getCompCode())
                .bind("propKey", role.getPropKey())
                .bind("propValue", Parameters.in(R2dbcType.VARCHAR, role.getPropValue()))
                .bind("remark", Parameters.in(R2dbcType.VARCHAR, role.getRemark()))
                .bind("updateDate", LocalDateTime.now())
                .fetch().rowsUpdated().thenReturn(role.buildDto());
    }

    public Mono<SystemPropertyDto> findById(String propKey, String compCode) {
        return template.select(SystemProperty.class)
                .matching(Query.query(Criteria.where("prop_key")
                        .is(propKey)
                        .and("comp_code").is(compCode)))
                .one()
                .map(SystemProperty::buildDto);
    }

    public Flux<SystemPropertyDto> getSystemPropertyByDate(String updatedDate) {
        return template.select(SystemProperty.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all().map(SystemProperty::buildDto);
    }

    public Flux<SystemPropertyDto> getSystemProperty(String compCode) {
        return template.select(SystemProperty.class)
                .matching(Query.query(Criteria.where("comp_code")
                        .is(compCode)))
                .all()
                .map(SystemProperty::buildDto);
    }

    public Flux<HashMap<String, String>> getProperty(String compCode, String roleCode, Integer macId) {
        HashMap<String, String> hm = new HashMap<>();
        return getSystemProperty(compCode)
                .flatMap(property -> {
                    String key = property.getKey().getPropKey();
                    String value = property.getPropValue();
                    if (!Util1.isNullOrEmpty(value) && !value.equals("-")) {
                        hm.put(key, value);
                    }
                    return Mono.from(rolePropertyService.getRoleProperty(roleCode, compCode));
                })
                .flatMap(roleProperty -> {
                    String key = roleProperty.getKey().getPropKey();
                    String value = roleProperty.getPropValue();
                    if (!Util1.isNullOrEmpty(value) && !value.equals("-")) {
                        hm.put(key, value);
                    }
                    return Mono.from(machinePropertyService.getMacProperty(macId));
                })
                .map(machineProperty -> {
                    String key = machineProperty.getKey().getPropKey();
                    String value = machineProperty.getPropValue();
                    if (!Util1.isNullOrEmpty(value) && !value.equals("-")) {
                        hm.put(key, value);
                    }
                    return hm;
                }).switchIfEmpty(Mono.just(hm));
    }


}
