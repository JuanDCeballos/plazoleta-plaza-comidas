package co.juan.plazacomidas.api.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private String errorMessage;
    private T data;

    public ApiResponse(T data) {
        this.success = true;
        this.errorMessage = null;
        this.data = data;
    }
}
