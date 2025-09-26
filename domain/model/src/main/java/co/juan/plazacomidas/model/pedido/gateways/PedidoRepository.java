package co.juan.plazacomidas.model.pedido.gateways;

import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.model.utils.EstadoPedido;

import java.util.Optional;

public interface PedidoRepository {

    Optional<Pedido> buscarPorId(Long idPedido);

    Pedido guardarPedido(Pedido pedido);

    Pedido actualizarPedido(Pedido pedido);

    boolean clienteTienePedidoActivo(Long idCliente);

    Pagina<Pedido> listarPedidosPorRestauranteYEstado(
            Long idRestaurante, Optional<EstadoPedido> estado, int page, int size);
}
