package core.user.api.dto;

import core.user.api.model.MachineInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
public class MachineInfoDto {
    private Integer macId;
    private String machineName;
    private String machineIp;
    private LocalDateTime updatedDate;
    private Boolean proUpdate;
    private String macAddress;
    private String serialNo;
    private String osName;
    private String osVersion;
    private String osArch;
    public MachineInfo toEntity(){
        return MachineInfo.builder()
                .macId(getMacId())
                .macName(getMachineName())
                .macIp(getMachineIp())
                .proUpdate(getProUpdate())
                .macAddress(getMacAddress())
                .serialNo(getSerialNo())
                .osName(getOsName())
                .osVersion(getOsVersion())
                .osArch(getOsArch())
                .build();
    }


}
