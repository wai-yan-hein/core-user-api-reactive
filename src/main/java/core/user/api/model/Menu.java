package core.user.api.model;

import core.user.api.dto.MenuDto;
import core.user.api.dto.MenuKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class Menu {
    @Id
    private String menuCode;
    private String compCode;
    private String userCode;
    private String menuClass;
    private String menuName;
    private String menuUrl;
    private String parentMenuCode;
    private String menuType;
    private String account;
    private Integer orderBy;
    private LocalDateTime updatedDate;
    private Integer menuVersion;

    public MenuDto buildDto() {
        return MenuDto.builder()
                .key(MenuKey.builder()
                        .menuCode(getMenuCode())
                        .compCode(getCompCode())
                        .build())
                .userCode(getUserCode())
                .menuClass(getMenuClass())
                .menuName(getMenuName())
                .menuUrl(getMenuUrl())
                .parentMenuCode(getParentMenuCode())
                .menuType(getMenuType())
                .account(getAccount())
                .orderBy(getOrderBy())
                .menuVersion(getMenuVersion())
                .build();
    }
}
