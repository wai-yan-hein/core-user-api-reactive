package core.user.api.dto;

import core.user.api.model.MachineProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Data
@Builder
public class MachinePropertyDto {
    private MachinePropertyKey key;
    private String propValue;
    private String remark;
    private LocalDateTime updatedDate;

    public MachineProperty toEntity() {
        return MachineProperty.builder()
                .propKey(getKey().getPropKey())
                .macId(getKey().getMacId())
                .propValue(getPropValue())
                .remark(getRemark())
                .updatedDate(getUpdatedDate())
                .build();
    }
}
