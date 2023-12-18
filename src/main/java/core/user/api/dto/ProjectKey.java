package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ProjectKey {
    private String projectNo;
    private String compCode;
}
