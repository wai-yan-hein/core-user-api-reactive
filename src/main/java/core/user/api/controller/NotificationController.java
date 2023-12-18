package core.user.api.controller;

import core.user.api.dto.NotificationDto;
import core.user.api.model.Notification;
import core.user.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    private Mono<Notification> save(@RequestBody NotificationDto notification) {
        return notificationService.save(notification);
    }

    @GetMapping("/{userId}")
    private Flux<Notification> save(@PathVariable String userId) {
        return notificationService.getNotificationByUser(userId);
    }

    @PostMapping("/updateNotification")
    private Mono<Boolean> updateNotification(@RequestBody NotificationDto obj) {
        return notificationService.updateNotification(obj);
    }
}
