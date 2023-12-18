package core.user.api.model;

import core.user.api.dto.MachinePropertyDto;
import core.user.api.dto.MachinePropertyKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@Builder
public class MachineProperty {
    @Id
    private Integer macId;
    private String propKey;
    private String propValue;
    private String remark;
    private LocalDateTime updatedDate;

    public MachinePropertyDto buildDto() {
        return MachinePropertyDto.builder()
                .key(MachinePropertyKey.builder()
                        .macId(getMacId())
                        .propKey(getPropKey())
                        .build())
                .propValue(getPropValue())
                .remark(getRemark())
                .updatedDate(getUpdatedDate())
                .build();
    }
}
