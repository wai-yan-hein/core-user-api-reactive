package core.user.api.auth;

import core.user.api.common.Util1;
import core.user.api.dto.MachineInfoDto;
import core.user.api.model.AppUser;
import core.user.api.model.MachineInfo;
import core.user.api.model.Token;
import core.user.api.model.TokenType;
import core.user.api.security.JWTReactiveAuthenticationManager;
import core.user.api.service.MachineService;
import core.user.api.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final MachineService machineService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final JWTReactiveAuthenticationManager authenticationManager;

    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        String serialNo = Util1.cleanStr(request.getSerialNo());
        return machineService.findBySerialNo(serialNo)
                .map(MachineInfoDto::toEntity)
                .flatMap(user -> {
                    String password = Util1.isNull(request.getPassword(), "-");
                    if (!password.equals(Util1.getPassword())) {
                        return Mono.just(AuthenticationResponse.builder().message("Authentication Failed.").build());
                    }

                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(serialNo, request.getPassword()));

                    var jwtToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);

                    revokeAllMachineTokens(user);
                    saveMachineToken(user, jwtToken.getAccessToken());

                    return Mono.just(AuthenticationResponse.builder()
                            .accessToken(jwtToken.getAccessToken())
                            .accessTokenExpired(jwtToken.getAccessTokenExpired())
                            .refreshToken(refreshToken.getRefreshToken())
                            .refreshTokenExpired(refreshToken.getRefreshTokenExpired())
                            .macId(user.getMacId())
                            .build());
                }).switchIfEmpty(Mono.error(new EmptyResultDataAccessException(1))); // 1 is the expected size, adjust as needed

    }

    public Mono<AuthenticationResponse> authenticateByUser(AppUser user) {
        String userName = user.getUsername();
        String password = user.getPassword();
        return Mono.fromRunnable(() -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password)))
                .then(Mono.defer(() -> {
                    var jwtToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);
                    return Mono.just(AuthenticationResponse.builder()
                            .accessToken(jwtToken.getAccessToken())
                            .accessTokenExpired(jwtToken.getAccessTokenExpired())
                            .refreshToken(refreshToken.getRefreshToken())
                            .refreshTokenExpired(refreshToken.getRefreshTokenExpired())
                            .build());
                }));
    }


    private void saveMachineToken(MachineInfo mac, String jwtToken) {
        var token = Token.builder()
                .macId(mac.getMacId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER.name())
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllMachineTokens(MachineInfo info) {
        tokenService.findAllValidTokenByMacId(info.getMacId())
                .flatMap(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                    return tokenService.saveToken(token);
                })
                .subscribe(); // Ensure that the update is executed. You may need to handle errors appropriately.
    }
}
