package core.user.api.controller;

import core.user.api.auth.AuthenticationRequest;
import core.user.api.auth.AuthenticationResponse;
import core.user.api.auth.AuthenticationService;
import core.user.api.common.Util1;
import core.user.api.dto.AppUserDto;
import core.user.api.dto.MachineInfoDto;
import core.user.api.service.AppUserService;
import core.user.api.service.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;
    private final MachineService machineService;
    private final AppUserService appUserService;

    @GetMapping("/login")
    public Mono<ResponseEntity<AppUserDto>> login(@RequestParam String userName,
                                                  @RequestParam String password) {
        return appUserService.login(userName, password);
    }

    @PostMapping("/authenticate")
    public Mono<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return service.authenticate(request);
    }

    @GetMapping("/checkSerialNo")
    public Mono<MachineInfoDto> checkSerialNo(@RequestParam String serialNo) {
        serialNo = Util1.cleanStr(serialNo);
        log.info("/checkSerialNo : " + serialNo);
        return machineService.findBySerialNo(serialNo);
    }

    @PostMapping("/registerMac")
    public Mono<AuthenticationRequest> registerMac(@RequestBody MachineInfoDto dto) {
        String serialNo = dto.getSerialNo();
        if (serialNo != null) {
            serialNo = Util1.cleanStr(serialNo);
            dto.setSerialNo(serialNo);
            log.info("/registerMac : " + serialNo);
            return machineService.saveOrUpdate(dto).flatMap(info -> {
                var request = AuthenticationRequest.builder()
                        .serialNo(info.getSerialNo())
                        .password(Util1.getPassword())
                        .build();

                return service.authenticate(request)
                        .map(authenticationResponse -> request);
            });
        }
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/hello")
    private Mono<String> hello() {
        return Mono.just("Hello");
    }
}
