package co.juan.plazacomidas.model.pedido;

import co.juan.plazacomidas.model.pedidoplato.PedidoPlato;
import co.juan.plazacomidas.model.utils.EstadoPedido;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Pedido {

    private Long id;
    private Long idCliente;
    private LocalDate fecha;
    private EstadoPedido estado;
    private Long idChef;
    private Long idRestaurante;
    private List<PedidoPlato> platos;
}
