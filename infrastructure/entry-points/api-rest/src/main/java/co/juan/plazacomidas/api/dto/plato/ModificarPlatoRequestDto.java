package co.juan.plazacomidas.api.dto.plato;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModificarPlatoRequestDto {

    @Min(value = 1, message = "El precio debe ser mayor a cero")
    @Positive(message = "El precio debe ser un número positivo")
    private BigDecimal precio;

    @NotBlank(message = "Si se proporciona una descripción, no puede estar en blanco")
    private String descripcion;
}
