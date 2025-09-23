package co.juan.plazacomidas.model.usuario.gateways;

import co.juan.plazacomidas.model.usuario.Usuario;

import java.util.Optional;

public interface UsuarioGateway {

    Optional<Usuario> obtenerUsuarioPorId(Long idUsuario);

    Optional<Usuario> obtenerUsuarioPorCorreo(String correo);
}
