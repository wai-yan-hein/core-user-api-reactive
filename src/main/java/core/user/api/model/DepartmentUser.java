package core.user.api.model;

import core.user.api.dto.DepartmentUserDto;
import core.user.api.dto.DepartmentUserKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@Builder
public class DepartmentUser {
    @Id
    private Integer deptId;
    private String compCode;
    private String userCode;
    private String deptName;
    private LocalDateTime updatedDate;
    private String phoneNo;
    private String address;
    private String email;
    private Boolean active;
    private boolean deleted;

    public DepartmentUserDto buildDto() {
        return DepartmentUserDto.builder()
                .key(DepartmentUserKey.builder()
                        .deptId(getDeptId())
                        .compCode(getCompCode())
                        .build())
                .userCode(getUserCode())
                .deptName(getDeptName())
                .updatedDate(getUpdatedDate())
                .phoneNo(getPhoneNo())
                .email(getEmail())
                .active(getActive())
                .updatedDate(getUpdatedDate())
                .build();
    }
}
