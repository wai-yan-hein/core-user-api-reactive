/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.user.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lenovo
 */
@Data
@Builder
public class RolePropertyKey {
    private String roleCode;
    private String propKey;
    private String compCode;

}
