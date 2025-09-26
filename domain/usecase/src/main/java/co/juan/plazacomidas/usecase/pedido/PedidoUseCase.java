package co.juan.plazacomidas.usecase.pedido;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.model.pedido.gateways.PedidoRepository;
import co.juan.plazacomidas.model.pedidoplato.PedidoPlato;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.model.restauranteempleado.gateways.RestauranteEmpleadoRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import co.juan.plazacomidas.model.utils.EstadoPedido;
import co.juan.plazacomidas.model.utils.MensajesEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class PedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final RestauranteEmpleadoRepository restauranteEmpleadoRepository;
    private final RestauranteRepository restauranteRepository;
    private final PlatoRepository platoRepository;
    private final UsuarioGateway usuarioGateway;

    public Pedido crearPedido(String emailCliente, Pedido pedido) {
        Usuario cliente = obtenerUsuarioPorCorreo(emailCliente);
        pedido.setIdCliente(cliente.getIdUsuario());

        if (pedidoRepository.clienteTienePedidoActivo(pedido.getIdCliente())) {
            throw new IllegalArgumentException(MensajesEnum.PEDIDO_EN_PROCESO.getMensaje());
        }

        if (!restauranteRepository.existePorId(pedido.getIdRestaurante())) {
            throw new ResourceNotFoundException(
                    MensajesEnum.RESTAURANTE_NO_ENCONTRADO.getMensaje() + pedido.getIdRestaurante());
        }

        for (PedidoPlato pp : pedido.getPlatos()) {
            Plato plato = platoRepository.buscarPorId(pp.getIdPlato())
                    .orElseThrow(() -> new ResourceNotFoundException(MensajesEnum.PLATO_NO_ENCONTRADO.getMensaje() +
                            pp.getIdPlato()));
            if (!plato.getIdRestaurante().equals(pedido.getIdRestaurante())) {
                throw new IllegalArgumentException(MensajesEnum.PLATO.getMensaje() + plato.getNombre() +
                        MensajesEnum.NO_PERTECENE_A_RESTAURANTE.getMensaje());
            }
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDate.now());

        return pedidoRepository.guardarPedido(pedido);
    }

    public Pagina<Pedido> listarPedidosPorEstado(
            String emailEmpleado, Optional<EstadoPedido> estado, int page, int size) {
        Usuario empleado = obtenerUsuarioPorCorreo(emailEmpleado);

        RestauranteEmpleado restauranteEmpleado = restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(empleado.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException(MensajesEnum.NO_TIENE_RESTAURANTE_ASIGNADO.getMensaje()));

        return pedidoRepository.listarPedidosPorRestauranteYEstado(
                restauranteEmpleado.getIdRestaurante(), estado, page, size);
    }

    private Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioGateway.obtenerUsuarioPorCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.USUARIO_NO_ENCONTRADO_POR_EMAIL.getMensaje() + correo));
    }
}
