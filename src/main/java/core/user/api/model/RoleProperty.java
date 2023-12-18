/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.user.api.model;

import core.user.api.dto.RolePropertyDto;
import core.user.api.dto.RolePropertyKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author Lenovo
 */
@Data
@Builder
public class RoleProperty {
    @Id
    private String roleCode;
    private String propKey;
    private String compCode;
    private String propValue;
    private String remark;
    private LocalDateTime updatedDate;

    public RolePropertyDto buildDto() {
        return RolePropertyDto.builder()
                .key(RolePropertyKey.builder()
                        .roleCode(getRoleCode())
                        .propKey(getPropKey())
                        .compCode(getCompCode())
                        .build())
                .propValue(getPropValue())
                .remark(getRemark())
                .updatedDate(getUpdatedDate())
                .build();
    }

}
