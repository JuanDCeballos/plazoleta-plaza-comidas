package co.juan.plazacomidas.consumer.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private boolean success;
    private String errorMessage;
    private UsuarioResponseDto data;
}
