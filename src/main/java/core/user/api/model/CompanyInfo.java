package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class CompanyInfo {
    @Id
    private String compCode;
    private String userCode;
    private String compName;
    private String compAddress;
    private String compPhone;
    private String compEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String curCode;
    private boolean active;
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer busId;
    private boolean batchLock;
    private LocalDate yearEndDate;
    private LocalDateTime updatedDate;
    private String securityCode;
}
