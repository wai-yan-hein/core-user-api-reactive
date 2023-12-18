package core.user.api.common;

import lombok.Data;

@Data
public class UserFilter {
    private String fromDate;
    private String toDate;
    private String homeCur;
    private String targetCur;
    private String compCode;
    private Boolean deleted;
}
