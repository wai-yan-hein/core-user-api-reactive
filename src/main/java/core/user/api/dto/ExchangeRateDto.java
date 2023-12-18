package core.user.api.dto;

import core.user.api.model.ExchangeRate;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ExchangeRateDto {
    private ExchangeKey key;
    private LocalDateTime exDate;
    private Double homeFactor;
    private String homeCur;
    private Double targetFactor;
    private String targetCur;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime updatedDate;
    private String updatedBy;
    private Double exRate;
    private Boolean deleted;
    public ExchangeRate toEntity(){
        return ExchangeRate.builder()
                .exCode(getKey().getExCode())
                .compCode(getKey().getCompCode())
                .homeFactor(getHomeFactor())
                .homeCur(getHomeCur())
                .targetFactor(getTargetFactor())
                .targetCur(getTargetCur())
                .createdDate(getCreatedDate())
                .createdBy(getCreatedBy())
                .updatedDate(getUpdatedDate())
                .updatedBy(getUpdatedBy())
                .exDate(getExDate())
                .deleted(getDeleted())
                .build();
    }
}
