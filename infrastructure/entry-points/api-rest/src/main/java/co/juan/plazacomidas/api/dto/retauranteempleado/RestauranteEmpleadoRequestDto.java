package co.juan.plazacomidas.api.dto.retauranteempleado;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestauranteEmpleadoRequestDto {

    @NotNull(message = "El ID del usuario empleado es obligatorio")
    private Long idUsuarioEmpleado;
}
