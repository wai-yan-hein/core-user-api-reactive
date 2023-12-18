package core.user.api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationUserJoin {
    @Id
    private String notificationId;
    private String userId;
    private boolean seen;
    private LocalDateTime updatedDate;
}
