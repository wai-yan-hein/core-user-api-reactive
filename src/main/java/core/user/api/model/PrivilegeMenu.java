package core.user.api.model;

import core.user.api.dto.PrivilegeMenuDto;
import core.user.api.dto.PrivilegeMenuKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class PrivilegeMenu {
    @Id
    private String roleCode;
    private String menuCode;
    private String compCode;
    private boolean allow;
    private LocalDateTime updatedDate;

    public PrivilegeMenuDto buildDto() {
        return PrivilegeMenuDto.builder()
                .key(PrivilegeMenuKey.builder()
                        .roleCode(getRoleCode())
                        .compCode(getCompCode())
                        .menuCode(getMenuCode())
                        .build())
                .allow(isAllow())
                .updatedDate(getUpdatedDate())
                .build();
    }
}
