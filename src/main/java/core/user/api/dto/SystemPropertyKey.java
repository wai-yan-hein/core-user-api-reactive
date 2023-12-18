package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SystemPropertyKey {
    private String propKey;
    private String compCode;


}
