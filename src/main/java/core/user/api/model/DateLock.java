package core.user.api.model;

import core.user.api.common.Util1;
import core.user.api.dto.DateLockDto;
import core.user.api.dto.DateLockKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
public class DateLock {
    @Id
    private String lockCode;
    private String compCode;
    private String remark;
    private Date startDate;
    private Date endDate;
    private boolean dateLock;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public DateLockDto buildDto() {
        return DateLockDto.builder()
                .key(DateLockKey.builder()
                        .lockCode(getLockCode())
                        .compCode(getCompCode())
                        .build())
                .remark(getRemark())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .dateLock(isDateLock())
                .createdBy(getCreatedBy())
                .updatedBy(getUpdatedBy())
                .createdDateTime(Util1.toZonedDateTime(getCreatedDate()))
                .updatedDateTime(Util1.toZonedDateTime(getUpdatedDate()))
                .build();
    }

}
