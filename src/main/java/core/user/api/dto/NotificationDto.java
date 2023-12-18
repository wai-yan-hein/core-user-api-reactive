package core.user.api.dto;

import core.user.api.model.Notification;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class NotificationDto {
    private String id;
    private LocalDateTime notificationDate;
    private String title;
    private String message;
    private String refNo;
    private String userId;
    private Boolean seen;
    private List<String> listUser;

    public Notification toEntity() {
        return Notification.builder()
                .id(getId())
                .notificationDate(getNotificationDate())
                .title(getTitle())
                .message(getMessage())
                .refNo(getRefNo())
                .refNo(getRefNo())
                .build();
    }
}
