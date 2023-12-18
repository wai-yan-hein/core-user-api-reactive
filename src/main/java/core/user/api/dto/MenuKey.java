package core.user.api.dto;

import core.user.api.model.Menu;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MenuKey {
    private String menuCode;
    private String compCode;

}
