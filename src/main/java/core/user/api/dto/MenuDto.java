package core.user.api.dto;

import core.user.api.model.Menu;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MenuDto {
    private MenuKey key;
    private String menuName;
    private String roleCode;
    private String userCode;
    private String menuClass;
    private String menuUrl;
    private String parentMenuCode;
    private String menuType;
    private String account;
    private Integer orderBy;
    private LocalDateTime updatedDate;
    private Integer menuVersion;
    private List<MenuDto> child;
    private Boolean allow;
    public Menu toEntity(){
        return Menu.builder()
                .menuCode(getKey().getMenuCode())
                .compCode(getKey().getCompCode())
                .userCode(getUserCode())
                .menuClass(getMenuClass())
                .menuName(getMenuName())
                .menuUrl(getMenuUrl())
                .parentMenuCode(getParentMenuCode())
                .menuType(getMenuType())
                .account(getAccount())
                .orderBy(getOrderBy())
                .updatedDate(getUpdatedDate())
                .menuVersion(getMenuVersion())
                .build();
    }

}
