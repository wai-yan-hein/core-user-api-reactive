package core.user.api.dto;

import core.user.api.model.SystemProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SystemPropertyDto {
    private SystemPropertyKey key;
    private String propValue;
    private String remark;
    private LocalDateTime updatedDate;
    public SystemProperty toEntity(){
        return SystemProperty.builder()
                .propKey(getKey().getPropKey())
                .compCode(getKey().getCompCode())
                .propValue(getPropValue())
                .remark(getRemark())
                .updatedDate(getUpdatedDate())
                .build();
    }

}
