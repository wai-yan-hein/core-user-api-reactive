package core.user.api.model;

import core.user.api.dto.AppUserDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Builder
@Data
public class AppUser implements UserDetails {
    @Id
    private String userCode;
    private String userLongName;
    private String userShortName;
    private String email;
    private String password;
    private boolean active;
    private String roleCode;
    private String doctorId;
    private Integer deptId;
    private LocalDateTime updatedDate;
    private String locCode;
    private String deptCode;

    public AppUserDto buildDto() {
        return AppUserDto.builder()
                .userCode(getUserCode())
                .userLongName(getUserLongName())
                .userShortName(getUserShortName())
                .password(getPassword())
                .email(getEmail())
                .roleCode(getRoleCode())
                .deptId(getDeptId())
                .locCode(getLocCode())
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
        return password;
    }

    @Override
    public String getUsername() {
        return userLongName;
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
