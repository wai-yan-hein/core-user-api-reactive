package core.user.api.service;

import core.user.api.auth.AuthenticationService;
import core.user.api.common.Util1;
import core.user.api.dto.AppUserDto;
import core.user.api.model.AppUser;
import core.user.api.model.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final R2dbcEntityTemplate template;
    private final AuthenticationService service;
    private final SeqService seqService;

    public Mono<ResponseEntity<AppUserDto>> login(String userName, String password) {
        return template.select(AppUser.class)
                .matching(Query
                        .query(Criteria
                                .where("user_short_name").is(userName)
                                .and("password").is(password)))
                .one()
                .flatMap(user -> {
                    AppUserDto dto = user.buildDto();
                    return service.authenticateByUser(user)
                            .map(authResponse -> {
                                dto.setAuthResponse(authResponse);
                                return ResponseEntity.ok(dto);
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    public Mono<AppUser> findById(String userCode) {
        return template.select(AppUser.class)
                .matching(Query.query(Criteria.where("user_code").is(userCode)))
                .one();
    }

    public Mono<AppUser> saveOrUpdate(AppUser user) {
        user.setUpdatedDate(LocalDateTime.now());
        String userCode = user.getUserCode();
        if (Util1.isNullOrEmpty(userCode)) {
            return seqService.getNextCode("AppUser", 5).flatMap(seqNo -> {
                user.setUserCode(seqNo);
                return template.insert(user);
            });
        }
        return template.update(user);
    }

    public Flux<AppUser> getAppUserByDate(String updatedDate) {
        return template.select(AppUser.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all();
    }

    public Flux<AppUserDto> findAll() {
        return template.select(AppUser.class).all().map(AppUser::buildDto);
    }

    public Flux<String> findAllActive() {
        return template.select(AppUser.class)
                .matching(Query.query(Criteria.where("active").is(true)))
                .all().map(AppUser::getUserCode);
    }

}
