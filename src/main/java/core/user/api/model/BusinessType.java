package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class BusinessType {
    @Id
    private Integer busId;
    private String busName;
    private LocalDateTime updatedDate;
}
