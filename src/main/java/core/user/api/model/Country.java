package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Country {
    @Id
    private String code;
    private String countryName;
}
