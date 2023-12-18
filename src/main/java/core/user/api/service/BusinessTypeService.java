package core.user.api.service;

import core.user.api.model.BusinessType;
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
public class BusinessTypeService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;

    public Flux<BusinessType> findAll() {
        return template.select(BusinessType.class).all();
    }

    public Mono<BusinessType> saveOrUpdate(BusinessType businessType) {
        Integer busId = businessType.getBusId();
        businessType.setUpdatedDate(LocalDateTime.now());
        if (busId == null) {
            return findMaxBusId().flatMap(seqNo -> {
                businessType.setBusId(seqNo);
                return template.insert(businessType);
            });
        }
        return template.update(businessType);
    }

    public Mono<BusinessType> findById(Integer busId) {
        return template.select(BusinessType.class)
                .matching(Query.query(Criteria.where("bus_id")
                        .is(busId))).one();
    }

    public Flux<BusinessType> getBusinessTypeByDate(String updatedDate) {
        return template.select(BusinessType.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all();
    }

    public Mono<Integer> findMaxBusId() {
        return databaseClient.sql("select max(bus_id) bus_id from business_type")
                .map((row) -> row.get("bus_id", Integer.class)).one();
    }


}
