package co.juan.plazacomidas.model.pedidoplato;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PedidoPlato {

    private Long idPlato;
    private int cantidad;
}
