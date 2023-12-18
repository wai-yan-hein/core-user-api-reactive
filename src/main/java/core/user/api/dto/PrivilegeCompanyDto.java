package core.user.api.dto;

import core.user.api.model.PrivilegeCompany;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class PrivilegeCompanyDto {
    private PrivilegeMenuKey key;
    private String compName;
    private Boolean allow;
    private LocalDateTime updatedDate;

    public PrivilegeCompany toEntity() {
        return PrivilegeCompany.builder()
                .roleCode(getKey().getRoleCode())
                .compCode(getKey().getCompCode())
                .allow(getAllow())
                .updatedDate(getUpdatedDate())
                .build();
    }

}
