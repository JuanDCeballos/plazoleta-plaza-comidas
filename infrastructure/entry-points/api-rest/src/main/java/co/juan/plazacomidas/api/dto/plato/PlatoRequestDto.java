package co.juan.plazacomidas.api.dto.plato;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatoRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a cero")
    @Positive(message = "El precio debe ser un número positivo")
    private BigDecimal precio;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message = "La url de la imagen es obligatoria")
    private String urlImagen;

    @NotNull(message = "La categoria es obligatoria")
    private Long categoria;
}
