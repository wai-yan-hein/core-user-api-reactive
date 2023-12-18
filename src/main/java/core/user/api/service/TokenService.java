package core.user.api.service;

import core.user.api.model.MachineInfo;
import core.user.api.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final R2dbcEntityTemplate template;

    public Flux<Token> findAllValidTokenByMacId(Integer macId) {
        return template.select(Token.class)
                .matching(Query.query(where("mac_id").is(macId)))
                .all();
    }

    public Mono<Token> saveToken(Token token) {
        Integer macId = token.getMacId();
        return template.select(Token.class)
                .matching(Query.query(where("mac_id").is(macId)))
                .one().flatMap(exist -> template.update(token))
                .switchIfEmpty(template.insert(token));
    }
}
