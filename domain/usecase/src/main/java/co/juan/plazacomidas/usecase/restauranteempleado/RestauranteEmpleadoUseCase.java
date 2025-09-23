package co.juan.plazacomidas.usecase.restauranteempleado;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.model.restauranteempleado.gateways.RestauranteEmpleadoRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteEmpleadoUseCase {

    private final RestauranteEmpleadoRepository restauranteEmpleadoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioGateway usuarioGateway;

    public RestauranteEmpleado crearRestauranteEmpleado(String emailPropietarioLogueado,
                                                        Long idRestaurante, RestauranteEmpleado restauranteEmpleado) {
        Usuario propietarioLogueado = usuarioGateway.obtenerUsuarioPorCorreo(emailPropietarioLogueado)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con email: " + emailPropietarioLogueado));

        Restaurante restaurante = restauranteRepository.obtenerById(idRestaurante);

        if (restaurante == null) {
            throw new ResourceNotFoundException("Restaurante no encontrado con id: " + idRestaurante);
        }

        if (!restaurante.getIdUsuario().equals(propietarioLogueado.getIdUsuario())) {
            throw new IllegalArgumentException("No tienes permiso para asignar empleados a este restaurante.");
        }

        Usuario empleado = usuarioGateway.obtenerUsuarioPorId(restauranteEmpleado.getIdUsuarioEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario que intentas asignar como empleado no existe."));

        if (!empleado.getIdRol().equals(3L)) {
            throw new IllegalArgumentException("El usuario proporcionado no tiene el rol de empleado.");
        }

        restauranteEmpleado.setIdRestaurante(idRestaurante);
        return restauranteEmpleadoRepository.guardar(restauranteEmpleado);
    }
}
