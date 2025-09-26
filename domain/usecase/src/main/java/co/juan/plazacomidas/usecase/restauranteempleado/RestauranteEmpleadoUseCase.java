package co.juan.plazacomidas.usecase.restauranteempleado;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.model.restauranteempleado.gateways.RestauranteEmpleadoRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import co.juan.plazacomidas.model.utils.MensajesEnum;
import co.juan.plazacomidas.model.utils.RolEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteEmpleadoUseCase {

    private final RestauranteEmpleadoRepository restauranteEmpleadoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioGateway usuarioGateway;

    public RestauranteEmpleado crearRestauranteEmpleado(String emailPropietarioLogueado,
                                                        Long idRestaurante, RestauranteEmpleado restauranteEmpleado) {
        Usuario propietarioLogueado = usuarioGateway.obtenerUsuarioPorCorreo(emailPropietarioLogueado)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.USUARIO_NO_ENCONTRADO_POR_EMAIL.getMensaje() + emailPropietarioLogueado));

        Restaurante restaurante = restauranteRepository.obtenerById(idRestaurante);

        if (restaurante == null) {
            throw new ResourceNotFoundException(
                    MensajesEnum.RESTAURANTE_NO_ENCONTRADO.getMensaje() + idRestaurante);
        }

        if (!restaurante.getIdUsuario().equals(propietarioLogueado.getIdUsuario())) {
            throw new IllegalArgumentException(MensajesEnum.NO_TIENE_PERMISOS_PARA_ASIGNAR_EMPLEADO.getMensaje());
        }

        Usuario empleado = usuarioGateway.obtenerUsuarioPorId(restauranteEmpleado.getIdUsuarioEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.USUARIO_NO_ENCONTRADO_POR_ID.getMensaje() +
                                restauranteEmpleado.getIdUsuarioEmpleado()));

        if (!empleado.getIdRol().equals(RolEnum.EMPLEADO.getId())) {
            throw new IllegalArgumentException(MensajesEnum.ROL_EMPLEADO.getMensaje());
        }

        restauranteEmpleado.setIdRestaurante(idRestaurante);
        return restauranteEmpleadoRepository.guardar(restauranteEmpleado);
    }
}
