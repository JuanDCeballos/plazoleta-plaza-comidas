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
        Usuario cliente = usuarioGateway.obtenerUsuarioPorCorreo(emailCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con el email: " + emailCliente));
        pedido.setIdCliente(cliente.getIdUsuario());

        if (pedidoRepository.clienteTienePedidoActivo(pedido.getIdCliente())) {
            throw new IllegalArgumentException("No puede crear un nuevo pedido mientras tenga uno en proceso.");
        }

        if (!restauranteRepository.existePorId(pedido.getIdRestaurante())) {
            throw new ResourceNotFoundException("Restaurante no encontrado.");
        }

        for (PedidoPlato pp : pedido.getPlatos()) {
            Plato plato = platoRepository.buscarPorId(pp.getIdPlato())
                    .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + pp.getIdPlato()));
            if (!plato.getIdRestaurante().equals(pedido.getIdRestaurante())) {
                throw new IllegalArgumentException("El plato " + plato.getNombre() + " no pertenece a este restaurante.");
            }
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDate.now());

        return pedidoRepository.guardarPedido(pedido);
    }

    public Pagina<Pedido> listarPedidosPorEstado(
            String emailEmpleado, Optional<EstadoPedido> estado, int page, int size) {
        Usuario empleado = usuarioGateway.obtenerUsuarioPorCorreo(emailEmpleado)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con el email: " + emailEmpleado));

        RestauranteEmpleado restauranteEmpleado = restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(empleado.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("No estás asignado a ningún restaurante."));

        return pedidoRepository.listarPedidosPorRestauranteYEstado(
                restauranteEmpleado.getIdRestaurante(), estado, page, size);
    }
}
