package uz.devops.orderms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestData<T> implements Serializable {
    private Boolean reply = false;
    private String service;
    private String destination;
    private String username;
    private T data;

    private RequestData(Boolean bool, T data) {
        this.reply = bool;
        this.data = data;
    }

    public static RequestData<?> replyDATA(Object data) {
        return new RequestData<>(true, data);
    }
}
