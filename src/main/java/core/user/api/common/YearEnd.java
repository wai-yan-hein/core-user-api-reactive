package core.user.api.common;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class YearEnd {
    private String yeCompCode;
    private String compCode;
    private Date startDate;
    private Date endDate;
    private Date yearEndDate;
    private boolean batchLock;
    private boolean opening;
    private String createBy;
    private LocalDateTime createdDate;
    private String message;
}
