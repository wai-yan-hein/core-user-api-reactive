/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author waiyan
 */
@Data
@Builder
public class SeqTable {
    @Id
    private String option;
    private String period;
    private Integer seqNo;

}
