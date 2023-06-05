package uz.devops.orderms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO<T> {
    private Boolean success;
    private String message;
    private T data;

    public ResponseDTO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
