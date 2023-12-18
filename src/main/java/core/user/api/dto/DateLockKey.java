package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DateLockKey {
    private String lockCode;
    private String compCode;
}
