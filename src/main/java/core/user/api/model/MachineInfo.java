package core.user.api.model;

import core.user.api.dto.MachineInfoDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
public class MachineInfo implements UserDetails {
    @Id
    private Integer macId;
    private String macName;
    private String macIp;
    private LocalDateTime updatedDate;
    private boolean proUpdate;
    private String macAddress;
    private String serialNo;
    private String osName;
    private String osVersion;
    private String osArch;
    public MachineInfoDto buildDto(){
        return MachineInfoDto.builder()
                .macId(getMacId())
                .machineName(getMacName())
                .machineIp(getMacIp())
                .proUpdate(isProUpdate())
                .macAddress(getMacAddress())
                .serialNo(getSerialNo())
                .osName(getOsName())
                .osVersion(getOsVersion())
                .osArch(getOsArch())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return serialNo;
    }

    @Override
    public String getUsername() {
        return serialNo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
