package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class Currency {
    @Id
    private String curCode;
    private String currencyName;
    private String currencySymbol;
    private boolean active;
    private String curGainAcc;
    private String curLostAcc;
    private LocalDateTime updatedDate;

}
