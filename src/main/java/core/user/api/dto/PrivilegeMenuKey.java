package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PrivilegeMenuKey {
    private String roleCode;
    private String menuCode;
    private String compCode;

}
