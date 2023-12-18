package core.user.api.service;

import core.user.api.common.Util1;
import core.user.api.dto.MachineInfoDto;
import core.user.api.model.MachineInfo;
import core.user.api.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class MachineService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;
    public Mono<MachineInfoDto> findBySerialNo(String serialNo) {
        return template.select(MachineInfo.class)
                .matching(query(where("serial_no").is(serialNo)))
                .one().map(MachineInfo::buildDto);
    }

    public Mono<MachineInfoDto> insert(MachineInfoDto dto) {
        MachineInfo info =dto.toEntity();
        return template.insert(info).map(MachineInfo::buildDto);
    }

    public Mono<Integer> findMaxMacId() {
        return databaseClient.sql("select max(mac_id) mac_id from machine_info")
                .map((row) -> row.get("mac_id", Integer.class)).one();
    }

    public Mono<MachineInfoDto> saveOrUpdate(MachineInfoDto dto) {
        MachineInfo info = dto.toEntity();
        info.setSerialNo(Util1.cleanStr(info.getSerialNo()));
        info.setUpdatedDate(LocalDateTime.now());
        Integer macId = info.getMacId();
        if (Util1.isNullOrEmpty(macId)) {
            return findMaxMacId().flatMap(integer -> {
                info.setMacId(integer);
                return template.insert(info).map(MachineInfo::buildDto);
            });
        }
        return template.update(info).map(MachineInfo::buildDto);
    }

    public Flux<MachineInfoDto> findAll() {
        return template.select(MachineInfo.class).all().map(MachineInfo::buildDto);
    }

    public Mono<Boolean> deleteByMacId(Integer macId) {
        return template.delete(MachineInfo.class)
                .matching(Query.query(Criteria.where("mac_id").is(macId)))
                .all()
                .thenReturn(true);
    }

    public Mono<Boolean> updateAllMachine(boolean update) {
        return databaseClient
                .sql("update machine_info set pro_update = :update, updated_date = current_timestamp()")
                .bind("update", update)
                .fetch()
                .rowsUpdated()
                .map(rowsUpdated -> rowsUpdated > 0);
    }

    public Flux<MachineInfoDto> getMachineByDate(String updatedDate) {
        return template.select(MachineInfo.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(MachineInfo::buildDto);
    }


}
