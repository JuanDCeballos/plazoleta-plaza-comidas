package co.juan.plazacomidas.model.pedido.gateways;

import co.juan.plazacomidas.model.pedido.Pedido;

public interface PedidoRepository {
    
    Pedido guardarPedido(Pedido pedido);

    boolean clienteTienePedidoActivo(Long idCliente);
}
