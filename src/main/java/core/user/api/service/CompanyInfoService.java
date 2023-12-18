package core.user.api.service;

import core.user.api.common.Util1;
import core.user.api.common.YearEnd;
import core.user.api.model.BusinessType;
import core.user.api.model.CompanyInfo;
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
public class CompanyInfoService {
    private final R2dbcEntityTemplate template;
    private final SeqService seqService;

    public Mono<CompanyInfo> findById(String compCode) {
        return template.select(CompanyInfo.class).matching(Query.query(Criteria.where("comp_code").is(compCode))).one();
    }

    public Mono<CompanyInfo> saveOrUpdate(CompanyInfo info) {
        info.setUpdatedDate(LocalDateTime.now());
        String compCode = info.getCompCode();
        if (Util1.isNullOrEmpty(compCode)) {
            return seqService.getNextCode("Company", 5).flatMap(seqNo -> {
                info.setCompCode(seqNo);
                return template.insert(info);
            });
        }
        return template.update(info);
    }
    public Flux<CompanyInfo> getCompanyByDate(String updatedDate) {
        return template.select(CompanyInfo.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all();
    }

    public Flux<CompanyInfo> findAll() {
        return template.select(CompanyInfo.class).all();
    }

    public Mono<CompanyInfo> yearEnd(YearEnd end) {
        return null;
    }
}
