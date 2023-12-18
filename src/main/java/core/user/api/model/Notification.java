package core.user.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Notification {
    private String id;
    private LocalDateTime notificationDate;
    private String title;
    private String message;
    private String refNo;
}
