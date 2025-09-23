package co.juan.plazacomidas.api.dto.restaurante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestauranteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "El nombre no puede contener solo números")
    private String nombre;

    @NotNull(message = "El NIT es obligatorio")
    private Long nit;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^(\\d{10}|\\+\\d{12})$", message = "El número de teléfono debe tener 10 dígitos o incluir el prefijo internacional (ej. +57) seguido de 10 dígitos.")
    private String telefono;

    @NotBlank(message = "La url del logo es obligatoria")
    private String urlLogo;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;
}
