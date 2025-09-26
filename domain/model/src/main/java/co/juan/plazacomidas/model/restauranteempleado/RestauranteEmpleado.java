package co.juan.plazacomidas.model.restauranteempleado;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RestauranteEmpleado {

    private Long id;
    private Long idRestaurante;
    private Long idUsuarioEmpleado;
}
