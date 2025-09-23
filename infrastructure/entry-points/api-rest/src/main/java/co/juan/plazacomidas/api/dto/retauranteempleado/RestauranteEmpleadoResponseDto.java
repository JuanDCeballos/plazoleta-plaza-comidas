package co.juan.plazacomidas.api.dto.retauranteempleado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteEmpleadoResponseDto {

    private Long id;
    private Long idRestaurante;
    private Long idUsuarioEmpleado;
}
