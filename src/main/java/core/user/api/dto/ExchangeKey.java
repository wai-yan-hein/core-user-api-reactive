package core.user.api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@Data
public class ExchangeKey {
    private String exCode;
    private String compCode;

}
