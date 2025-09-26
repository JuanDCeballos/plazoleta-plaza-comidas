package co.juan.plazacomidas.api.dto.pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoPlatoResponseDto {

    private Long idPlato;
    private int cantidad;
}
