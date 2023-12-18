package core.user.api.service;

import core.user.api.common.Message;
import core.user.api.dto.NotificationDto;
import core.user.api.message.MessageProcessor;
import core.user.api.model.Notification;
import core.user.api.model.NotificationUserJoin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final R2dbcEntityTemplate template;
    private final DatabaseClient databaseClient;
    private final AppUserService appUserService;
    private final MessageProcessor messageProcessor;

    public Mono<Notification> save(NotificationDto dto) {
        Notification notification = dto.toEntity();
        notification.setNotificationDate(LocalDateTime.now());
        notification.setId(UUID.randomUUID().toString());
        return template.insert(notification)
                .flatMap(saved -> saveNotificationUser(saved.getId())
                        .flatMap(aBoolean -> {
                            sendMessage();
                            return Mono.just(saved);
                        }));
    }

    public void sendMessage() {
        messageProcessor.process(Message.builder()
                .header("NOTIFICATION")
                .build());
    }

    public Mono<Boolean> saveNotificationUser(String notificationId) {
        return appUserService.findAllActive().flatMap(userId -> {
                    var obj = NotificationUserJoin.builder()
                            .notificationId(notificationId)
                            .userId(userId)
                            .updatedDate(LocalDateTime.now())
                            .build();
                    return save(obj).thenReturn(true);
                }).defaultIfEmpty(false)
                .then(Mono.just(true));
    }

    private Mono<NotificationUserJoin> save(NotificationUserJoin userJoin) {
        return template.insert(userJoin);
    }

    public Flux<Notification> getNotificationByUser(String userId) {
        String sql = """
                select n.*
                from notification_user_join nu join notification n
                on nu.notification_id = n.id
                where nu.seen =false
                and nu.user_id = :userId
                """;
        //id, notification_date, title, message, ref_no
        return databaseClient.sql(sql)
                .bind("userId", userId)
                .map((row) -> Notification.builder()
                        .id(row.get("id", String.class))
                        .notificationDate(row.get("notification_date", LocalDateTime.class))
                        .title(row.get("title", String.class))
                        .refNo(row.get("ref_no", String.class))
                        .message(row.get("message", String.class))
                        .build()).all();
    }

    public Mono<Notification> findById(String id) {
        return template.select(Notification.class)
                .matching(Query.query(Criteria.where("id")
                        .is(id)))
                .one();
    }

    public Mono<NotificationUserJoin> findById(String notificationId, String userId) {
        return template.select(NotificationUserJoin.class)
                .matching(Query.query(Criteria.where("notification_id")
                        .is(notificationId)
                        .and("user_id").is(userId)))
                .one();
    }

    public Mono<Boolean> updateNotification(NotificationDto dto) {
        String id = dto.getId();
        String userId = dto.getUserId();
        boolean seen = dto.getSeen();
        return template.update(NotificationUserJoin.class)
                .matching(Query.query(Criteria.where("notification_id").is(id)
                        .and("user_id").is(userId)))
                .apply(Update.update("seen", seen).set("updated_date", LocalDateTime.now()))
                .thenReturn(true) // Return true if the update is successful
                .defaultIfEmpty(false); // Return false if no row was updated
    }

}
