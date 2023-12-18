package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentUserKey {
    private Integer deptId;
    private String compCode;
}
