package core.user.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.user.api.auth.AuthenticationResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUserDto {
    private String userCode;
    private String userLongName;
    private String userShortName;
    private String password;
    private String email;
    private String roleCode;
    private String doctorId;
    private Integer deptId;
    private String locCode;
    private AuthenticationResponse authResponse;
}
