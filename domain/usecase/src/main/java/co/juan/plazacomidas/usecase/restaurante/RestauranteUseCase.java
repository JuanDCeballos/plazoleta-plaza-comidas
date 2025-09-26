package co.juan.plazacomidas.usecase.restaurante;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import co.juan.plazacomidas.model.utils.MensajesEnum;
import co.juan.plazacomidas.model.utils.RolEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioGateway usuarioGateway;

    public Restaurante crearRestaurante(Restaurante restaurante) {

        Usuario usuario = usuarioGateway.obtenerUsuarioPorId(restaurante.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.USUARIO_NO_ENCONTRADO_POR_ID.getMensaje() + restaurante.getIdUsuario()));

        if (!usuario.getIdRol().equals(RolEnum.PROPIETARIO.getId())) {
            throw new IllegalArgumentException(MensajesEnum.ROL_PROPIETARIO.getMensaje());
        }

        return restauranteRepository.crearRestaurante(restaurante);
    }

    public Pagina<Restaurante> listarRestaurantes(int page, int size) {
        return restauranteRepository.listarRestaurantesPaginados(page, size);
    }
}
