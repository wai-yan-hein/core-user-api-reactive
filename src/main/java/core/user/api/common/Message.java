package core.user.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String header;
    private String entity;
    private String message;
}
