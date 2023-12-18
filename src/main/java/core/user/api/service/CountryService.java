package core.user.api.service;

import core.user.api.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CountryService {
    private final R2dbcEntityTemplate template;

    public Flux<Country> findAll() {
        return template.select(Country.class).all();
    }
    public Mono<Country> findById(String countryCode){
        return template.select(Country.class).matching(Query.query(Criteria.where("code").is(countryCode))).one();
    }
}
