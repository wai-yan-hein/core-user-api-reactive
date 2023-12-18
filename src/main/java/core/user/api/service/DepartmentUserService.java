package core.user.api.service;

import core.user.api.common.Util1;
import core.user.api.dto.DepartmentUserDto;
import core.user.api.dto.DepartmentUserKey;
import core.user.api.model.DepartmentUser;
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
public class DepartmentUserService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;


    public Mono<DepartmentUserDto> findById(Integer deptId, String compCode) {
        return template.select(DepartmentUser.class)
                .matching(Query.query(Criteria.where("dept_id").is(deptId)
                        .and("comp_code").is(compCode)))
                .one().map(DepartmentUser::buildDto);
    }

    public Mono<DepartmentUserDto> saveOrUpdate(DepartmentUserDto dto) {
        DepartmentUser user = dto.toEntity();
        user.setUpdatedDate(LocalDateTime.now());
        Integer deptId = user.getDeptId();
        if (Util1.isNullOrEmpty(deptId)) {
            return findMaxId().flatMap(seqNo -> {
                user.setDeptId(seqNo);
                return template.insert(user).map(DepartmentUser::buildDto);
            });
        }
        return update(user);
    }

    private Mono<DepartmentUserDto> update(DepartmentUser user) {
        String sql = """
                update department
                set user_code = :userCode,dept_name = :deptName,updated_date = :updateDate,
                phone=:phone,address=:address,email=:email,active=:active,deleted=:deleted
                where dept_id =:deptId and comp_code =:compCode
                """;
        return databaseClient.sql(sql)
                .bind("deptId", user.getDeptId())
                .bind("compCode", user.getCompCode())
                .bind("userCode", Parameters.in(R2dbcType.VARCHAR, user.getUserCode()))
                .bind("deptName", Parameters.in(R2dbcType.VARCHAR, user.getDeptName()))
                .bind("updateDate", LocalDateTime.now())
                .bind("phone", Parameters.in(R2dbcType.VARCHAR, user.getPhoneNo()))
                .bind("address", Parameters.in(R2dbcType.VARCHAR, user.getAddress()))
                .bind("email", Parameters.in(R2dbcType.VARCHAR, user.getEmail()))
                .bind("active", Parameters.in(R2dbcType.BOOLEAN, user.getActive()))
                .bind("deleted", Parameters.in(R2dbcType.BOOLEAN, user.getActive()))
                .fetch().rowsUpdated().thenReturn(user.buildDto());
    }

    public Mono<Integer> findMaxId() {
        return databaseClient.sql("select max(dept_id) dept_id from department")
                .map((row) -> row.get("dept_id", Integer.class)).one();
    }

    public Flux<DepartmentUserDto> getDepartmentByDate(String updatedDate) {
        return template.select(DepartmentUser.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all().map(DepartmentUser::buildDto);
    }

    public Flux<DepartmentUserDto> getDepartment(Boolean active, String compCode) {
        String sql = """
                select *
                from department
                where comp_code=:compCode
                and (active = :active or false = :active)
                and deleted = false
                """;
        //dept_id, user_code, dept_name, inv_queue,
        // acc_queue, updated_date, phone, address, email, active, deleted, comp_code
        return databaseClient.sql(sql)
                .bind("compCode", compCode)
                .bind("active", active)
                .map((row) -> DepartmentUserDto.builder()
                        .key(DepartmentUserKey.builder()
                                .deptId(row.get("dept_id", Integer.class))
                                .compCode(row.get("comp_code", String.class))
                                .build())
                        .userCode(row.get("user_code", String.class))
                        .deptName(row.get("dept_name", String.class))
                        .updatedDate(row.get("updated_date", LocalDateTime.class))
                        .phoneNo(row.get("phone", String.class))
                        .address(row.get("address", String.class))
                        .email(row.get("email", String.class))
                        .active(row.get("active", Boolean.class))
                        .build()).all();
    }
}
