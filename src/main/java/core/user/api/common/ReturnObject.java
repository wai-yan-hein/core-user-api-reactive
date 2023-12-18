/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.user.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ReturnObject {
    private String status;
    private String message;
    private String errorMessage;
    private List<Object> list;
    private Object data;
    private long timestampUtc;
    private byte[] file;
}
