package core.user.api.model;

import core.user.api.dto.ExchangeKey;
import core.user.api.dto.ExchangeRateDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@Data
public class ExchangeRate {
    @Id
    private String exCode;
    private String compCode;
    private LocalDateTime exDate;
    private Double homeFactor;
    private String homeCur;
    private Double targetFactor;
    private String targetCur;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;
    private boolean deleted;

    public ExchangeRateDto buildDto() {
        return ExchangeRateDto.builder()
                .key(ExchangeKey.builder()
                        .exCode(getExCode())
                        .compCode(getCompCode())
                        .build())
                .exDate(getExDate())
                .homeFactor(getHomeFactor())
                .homeCur(getHomeCur())
                .targetFactor(getTargetFactor())
                .targetCur(getTargetCur())
                .createdDate(getCreatedDate())
                .createdBy(getCreatedBy())
                .updatedDate(getUpdatedDate())
                .updatedBy(getUpdatedBy())
                .deleted(isDeleted())
                .build();
    }

}
