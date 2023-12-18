package core.user.api.service;

import core.user.api.common.UserFilter;
import core.user.api.dto.ExchangeKey;
import core.user.api.dto.ExchangeRateDto;
import core.user.api.model.ExchangeRate;
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
public class ExchangeRateService {
    private final R2dbcEntityTemplate template;
    private final SeqService seqService;
    private final DatabaseClient databaseClient;

    public Mono<ExchangeRateDto> saveOrUpdate(ExchangeRateDto dto) {
        ExchangeRate exchange = dto.toEntity();
        String exCode = exchange.getExCode();
        exchange.setUpdatedDate(LocalDateTime.now());
        if (exCode == null) {
            return seqService.getNextCode("Exchange", 5).flatMap(seqNo -> {
                exchange.setExCode(seqNo);
                return template.insert(exchange).map(ExchangeRate::buildDto);
            });
        }
        return template.update(exchange).map(ExchangeRate::buildDto);
    }

    public Flux<ExchangeRateDto> findAll(String compCode) {
        return template.select(ExchangeRate.class)
                .matching(Query.query(Criteria.where("comp_code")
                        .is(compCode)))
                .all()
                .map(ExchangeRate::buildDto);
    }

    public Mono<ExchangeRateDto> findById(String id, String compCode) {
        return template.select(ExchangeRate.class)
                .matching(Query.query(Criteria.where("ex_code").is(id)
                        .and("comp_code").is(compCode)))
                .one()
                .map(ExchangeRate::buildDto);
    }

    public Flux<ExchangeRateDto> getExchangeByDate(String updatedDate) {
        return template.select(ExchangeRate.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(ExchangeRate::buildDto);
    }

    public Flux<ExchangeRateDto> search(String startDate, String endDate, String targetCur, String compCode) {
        String sql = """
                select *,home_factor/target_factor ex_rate
                from exchange_rate
                where date(ex_date) between :startDate and :endDate
                and target_cur = :targetCur
                and comp_code =:compCode
                """;
        //ex_code, comp_code, ex_date, home_factor, home_cur,
        // target_factor, target_cur, created_date, created_by, updated_date, updated_by, deleted
        return databaseClient.sql(sql)
                .bind("startDate", startDate)
                .bind("endDate", endDate)
                .bind("targetCur", targetCur)
                .bind("compCode", compCode)
                .map((row) ->
                        ExchangeRateDto.builder()
                                .key(ExchangeKey.builder()
                                        .exCode(row.get("ex_code", String.class))
                                        .compCode(row.get("comp_code", String.class))
                                        .build())
                                .exDate(row.get("ex_date", LocalDateTime.class))
                                .homeFactor(row.get("home_factor", Double.class))
                                .homeCur(row.get("home_cur", String.class))
                                .targetFactor(row.get("target_factor", Double.class))
                                .targetCur(row.get("target_cur", String.class))
                                .createdBy(row.get("created_by", String.class))
                                .updatedBy(row.get("updated_by", String.class))
                                .exRate(row.get("ex_rate", Double.class))
                                .build())
                .all();
    }

    public Mono<Boolean> delete(String exCode, String compCode) {
        return template.select(ExchangeRate.class)
                .matching(Query.query(Criteria.where("ex_code").is(exCode)
                        .and("comp_code").is(compCode)))
                .one().thenReturn(true);
    }

    public Mono<ExchangeRateDto> getRecentRate(UserFilter filter) {
        String fromDate = filter.getFromDate();
        String toDate = filter.getToDate();
        String compCode = filter.getCompCode();
        String homeCur = filter.getHomeCur();
        String targetCur = filter.getTargetCur();
        String sql = """
                select home_cur,target_cur,round((home_factor/target_factor),3) recent_rate
                from exchange_rate
                where date(ex_date) between :startDate and :endDate
                and comp_code=:compCode
                and home_cur =:homeCur
                and target_cur=:targetCur
                and deleted =false
                order by ex_date desc
                limit 1;""";
        return databaseClient.sql(sql)
                .bind("startDate", fromDate)
                .bind("endDate", toDate)
                .bind("homeCur", homeCur)
                .bind("targetCur", targetCur)
                .bind("compCode", compCode)
                .map((row) -> ExchangeRateDto.builder()
                        .homeCur(row.get("home_cur", String.class))
                        .targetCur(row.get("target_cur", String.class))
                        .exRate(row.get("recent_rate", Double.class))
                        .build())
                .one();

    }

    public Mono<ExchangeRateDto> getAvgRate(UserFilter filter) {
        String fromDate = filter.getFromDate();
        String toDate = filter.getToDate();
        String compCode = filter.getCompCode();
        String homeCur = filter.getHomeCur();
        String targetCur = filter.getTargetCur();
        String sql = """
                select home_cur,target_cur,round(avg(home_factor/target_factor),3) avg_rate
                from exchange_rate
                where date(ex_date) between :startDate and :endDate
                and comp_code=:compCode
                and home_cur =:homeCur
                and target_cur=:targetCur
                and deleted =false
                group by home_cur;""";
        return databaseClient.sql(sql)
                .bind("startDate", fromDate)
                .bind("endDate", toDate)
                .bind("homeCur", homeCur)
                .bind("targetCur", targetCur)
                .bind("compCode", compCode)
                .map((row) -> ExchangeRateDto.builder()
                        .homeCur(row.get("home_cur", String.class))
                        .targetCur(row.get("target_cur", String.class))
                        .exRate(row.get("avg_rate", Double.class))
                        .build())
                .one();
    }
}
