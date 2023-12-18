package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class PrivilegeCompanyKey {
    private String roleCode;
    private String compCode;

}
