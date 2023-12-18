package core.user.api.service;

import core.user.api.dto.PrivilegeCompanyDto;
import core.user.api.dto.PrivilegeCompanyKey;
import core.user.api.dto.CompanyInfoDto;
import core.user.api.model.PrivilegeCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PrivilegeCompanyService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient client;

    public Mono<PrivilegeCompanyDto> saveOrUpdate(PrivilegeCompanyDto dto) {
        PrivilegeCompany role = dto.toEntity();
        String roleCode = role.getRoleCode();
        String compCode = role.getCompCode();
        return findById(roleCode, compCode)
                .flatMap(exist -> template.update(role)).map(PrivilegeCompany::buildDto)
                .switchIfEmpty(template.update(role).map(PrivilegeCompany::buildDto));
    }

    public Mono<PrivilegeCompanyDto> findById(String roleCode, String compCode) {
        return template.select(PrivilegeCompany.class).
                matching(Query.query(Criteria.where("role_code")
                        .is(roleCode).and("comp_code")
                        .is(compCode))).one().map(PrivilegeCompany::buildDto);
    }

    public Flux<CompanyInfoDto> getRoleCompany(String roleCode) {
        String sql = """
                select p.*,c.*
                from privilege_company p join company_info c
                on p.comp_code = c.comp_code
                where role_code =:roleCode""";
        //role_code, comp_code, allow, updated_date, name
        return client.sql(sql)
                .bind("roleCode", roleCode).map((row) -> CompanyInfoDto.builder()
                        .compCode(row.get("comp_code", String.class))
                        .roleCode(row.get("role_code", String.class))
                        .compName(row.get("comp_name", String.class))
                        .startDate(row.get("start_date", LocalDate.class))
                        .endDate(row.get("end_date", LocalDate.class))
                        .compAddress(row.get("comp_address", String.class))
                        .compPhone(row.get("comp_phone", String.class))
                        .compEmail(row.get("comp_phone", String.class))
                        .curCode(row.get("cur_code", String.class))
                        .batchLock(row.get("batch_lock", Boolean.class))
                        .active(row.get("active", Boolean.class))
                        .build()).all();
    }

    public Flux<PrivilegeCompanyDto> getProjectByDate(String updatedDate) {
        return template.select(PrivilegeCompany.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(PrivilegeCompany::buildDto);
    }
}
