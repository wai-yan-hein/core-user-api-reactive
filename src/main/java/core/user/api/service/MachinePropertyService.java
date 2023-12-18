package core.user.api.service;

import core.user.api.dto.MachinePropertyDto;
import core.user.api.model.MachineProperty;
import io.r2dbc.spi.Parameters;
import io.r2dbc.spi.R2dbcType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MachinePropertyService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;

    public Flux<MachinePropertyDto> getMachinePropertyByDate(String updatedDate) {
        return template.select(MachineProperty.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(MachineProperty::buildDto);
    }

    public Mono<MachinePropertyDto> saveOrUpdate(MachinePropertyDto dto) {
        MachineProperty property = dto.toEntity();
        String propKey = property.getPropKey();
        Integer macId = property.getMacId();
        return findById(propKey, macId)
                .flatMap(dto1 -> update(property))
                .switchIfEmpty(template.insert(property).map(MachineProperty::buildDto));
    }
    private Mono<MachinePropertyDto> update(MachineProperty property) {
        String sql = """
                update machine_property
                set prop_value = :propValue,remark = :remark,updated_date = :updateDate
                where mac_id =:macId and prop_key =:propKey
                """;
        return databaseClient.sql(sql)
                .bind("macId", property.getMacId())
                .bind("propKey", property.getPropKey())
                .bind("propValue", Parameters.in(R2dbcType.VARCHAR, property.getPropValue()))
                .bind("remark", Parameters.in(R2dbcType.VARCHAR, property.getRemark()))
                .bind("updateDate", LocalDateTime.now())
                .fetch().rowsUpdated().thenReturn(property.buildDto());
    }

    public Mono<MachinePropertyDto> findById(String propKey, Integer macId) {
        return template.select(MachineProperty.class)
                .matching(Query.query(Criteria.where("prop_key")
                        .is(propKey)
                        .and("mac_id")
                        .is(macId)))
                .one().map(MachineProperty::buildDto);
    }

    public Flux<MachinePropertyDto> getMacProperty(Integer macId) {
        return template.select(MachineProperty.class)
                .matching(Query.query(Criteria.where("mac_id")
                        .is(macId)))
                .all().map(MachineProperty::buildDto);
    }
}
