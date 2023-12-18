package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class AppRole {
    @Id
    private String roleCode;
    private String roleName;
    private LocalDateTime updatedDate;
}
