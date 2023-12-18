package core.user.api.model;

import core.user.api.dto.SystemPropertyDto;
import core.user.api.dto.SystemPropertyKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class SystemProperty {
    @Id
    private String propKey;
    private String compCode;
    private String propValue;
    private String remark;
    private LocalDateTime updatedDate;

    public SystemPropertyDto buildDto() {
        return SystemPropertyDto.builder()
                .key(SystemPropertyKey.builder()
                        .propKey(getPropKey())
                        .compCode(getCompCode())
                        .build())
                .propValue(getPropValue())
                .remark(getRemark())
                .updatedDate(getUpdatedDate())
                .build();
    }

}
