/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.user.api.dto;

import core.user.api.model.RoleProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author Lenovo
 */
@Data
@Builder
public class RolePropertyDto {
    private RolePropertyKey key;
    private String propValue;
    private String remark;
    private LocalDateTime updatedDate;

    public RoleProperty toEntity() {
        return RoleProperty.builder()
                .propKey(getKey().getPropKey())
                .compCode(getKey().getCompCode())
                .roleCode(getKey().getRoleCode())
                .propValue(getPropValue())
                .remark(getRemark())
                .updatedDate(getUpdatedDate())
                .build();
    }

}
