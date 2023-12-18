package core.user.api.service;

import core.user.api.dto.DateLockDto;
import core.user.api.model.DateLock;
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
public class DateLockService {
    private final R2dbcEntityTemplate template;

    public Flux<DateLockDto> findAll(String compCode) {
        return template.select(DateLock.class)
                .matching(Query.query(Criteria.where("comp_code")
                        .is(compCode)))
                .all().map(DateLock::buildDto);
    }

    public Mono<DateLockDto> saveOrUpdate(DateLock dl) {
        dl.setUpdatedDate(LocalDateTime.now());
        return findById(dl.getLockCode(), dl.getCompCode())
                .flatMap(dateLock -> template.update(dl).map(DateLock::buildDto))
                .switchIfEmpty(template.insert(dl).map(DateLock::buildDto));
    }

    public Mono<DateLockDto> findById(String lockCode, String compCode) {
        return template.select(DateLock.class)
                .matching(Query.query(Criteria.where("lock_code")
                        .is(lockCode).and("comp_code")
                        .is(compCode)))
                .one().map(DateLock::buildDto);
    }

    public Flux<DateLockDto> getDateLockByDate(String updatedDate) {
        return template.select(DateLock.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(DateLock::buildDto);
    }
}
