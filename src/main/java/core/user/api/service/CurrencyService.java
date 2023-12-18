package core.user.api.service;

import core.user.api.model.AppUser;
import core.user.api.model.CompanyInfo;
import core.user.api.model.Currency;
import core.user.api.model.ExchangeRate;
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
public class CurrencyService {
    private final R2dbcEntityTemplate template;
    private final SeqService seqService;

    public Mono<Currency> findById(String curCode) {
        return template.select(Currency.class).matching(Query.query(Criteria.where("cur_code").is(curCode))).one();
    }

    public Mono<Currency> saveOrUpdate(Currency currency) {
        currency.setUpdatedDate(LocalDateTime.now());
        return findById(currency.getCurCode())
                .flatMap(exist -> template.update(currency))
                .switchIfEmpty(template.insert(currency));
    }

    public Flux<Currency> findAll() {
        return template.select(Currency.class).all();
    }
    public Flux<Currency> getCurrencyByDate(String updatedDate) {
        return template.select(Currency.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all();
    }
}
