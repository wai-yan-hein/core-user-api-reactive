package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class MachinePropertyKey {
    private Integer macId;
    private String propKey;
}
