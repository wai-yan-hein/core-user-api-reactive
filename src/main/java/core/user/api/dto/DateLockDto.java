package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Builder
@Data
public class DateLockDto {
    private DateLockKey key;
    private String remark;
    private Date startDate;
    private Date endDate;
    private Boolean dateLock;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;

}
