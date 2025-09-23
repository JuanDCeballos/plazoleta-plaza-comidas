package co.juan.plazacomidas.model.usuario.gateways;

import co.juan.plazacomidas.model.usuario.Usuario;

public interface UsuarioGateway {

    Usuario obtenerUsuarioPorId(Long idUsuario);
}
