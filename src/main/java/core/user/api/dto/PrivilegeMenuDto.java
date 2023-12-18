package core.user.api.dto;

import core.user.api.model.PrivilegeMenu;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class PrivilegeMenuDto {
    private PrivilegeMenuKey key;
    private boolean allow;
    private LocalDateTime updatedDate;
    public PrivilegeMenu toEntity(){
        return PrivilegeMenu.builder()
                .menuCode(getKey().getMenuCode())
                .compCode(getKey().getCompCode())
                .allow(isAllow())
                .updatedDate(getUpdatedDate())
                .build();
    }

}
