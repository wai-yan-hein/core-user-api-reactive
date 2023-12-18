package core.user.api.dto;

import core.user.api.dto.PrivilegeCompanyKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class CompanyInfoDto {
    private PrivilegeCompanyKey key;
    private String roleCode;
    private String compCode;
    private String userCode;
    private String compName;
    private String compAddress;
    private String compPhone;
    private String compEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String curCode;
    private Boolean active;
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer busId;
    private Boolean batchLock;
    private LocalDate yearEndDate;
    private LocalDateTime updatedDate;
    private String securityCode;
}
