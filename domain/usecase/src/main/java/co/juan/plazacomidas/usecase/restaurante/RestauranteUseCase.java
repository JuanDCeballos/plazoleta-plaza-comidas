package co.juan.plazacomidas.usecase.restaurante;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioGateway usuarioGateway;

    public Restaurante crearRestaurante(Restaurante restaurante) {

        Usuario usuario = usuarioGateway.obtenerUsuarioPorId(restaurante.getIdUsuario());

        if (usuario == null) {
            throw new ResourceNotFoundException("El usuario con el ID proporcionado no existe.");
        }

        if (usuario.getIdRol() != 2) {
            throw new IllegalArgumentException("El rol debe ser propietario");
        }

        return restauranteRepository.crearRestaurante(restaurante);
    }
}
