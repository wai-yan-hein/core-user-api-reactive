package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;

@Data
@Builder
public class Token {
    @Id
    public Integer macId;
    public String token;
    public String tokenType;
    public boolean revoked;
    public boolean expired;

}
