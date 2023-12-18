package core.user.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.user.api.dto.PrivilegeCompanyDto;
import core.user.api.dto.PrivilegeMenuKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrivilegeCompany {
    @Id
    private String roleCode;
    private String compCode;
    private boolean allow;
    private LocalDateTime updatedDate;

    public PrivilegeCompanyDto buildDto() {
        return PrivilegeCompanyDto.builder()
                .key(PrivilegeMenuKey.builder()
                        .roleCode(getRoleCode())
                        .compCode(getCompCode())
                        .build())
                .allow(isAllow())
                .build();
    }

}
