package co.juan.plazacomidas.model.restauranteempleado.gateways;

import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;

import java.util.Optional;

public interface RestauranteEmpleadoRepository {

    RestauranteEmpleado guardar(RestauranteEmpleado restauranteEmpleado);

    Optional<RestauranteEmpleado> buscarByIdUsuarioEmpleado(Long idUsuarioEmpleado);
}
