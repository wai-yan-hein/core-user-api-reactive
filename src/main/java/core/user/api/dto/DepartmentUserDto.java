package core.user.api.dto;

import core.user.api.model.DepartmentUser;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@Builder
public class DepartmentUserDto {
    private DepartmentUserKey key;
    private String userCode;
    private String deptName;
    private LocalDateTime updatedDate;
    private String phoneNo;
    private String address;
    private String email;
    private Boolean active;
    private Boolean deleted;
    public DepartmentUser toEntity(){
        return DepartmentUser.builder()
                .deptId(getKey().getDeptId())
                .compCode(getKey().getCompCode())
                .userCode(getUserCode())
                .deptName(getDeptName())
                .updatedDate(getUpdatedDate())
                .phoneNo(getPhoneNo())
                .address(getAddress())
                .email(getEmail())
                .active(getActive())
                .deleted(getDeleted())
                .build();
    }
}
