package core.user.api.security;

import lombok.*;

/**
 * @author duc-d
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTToken {
	private String token;
}
