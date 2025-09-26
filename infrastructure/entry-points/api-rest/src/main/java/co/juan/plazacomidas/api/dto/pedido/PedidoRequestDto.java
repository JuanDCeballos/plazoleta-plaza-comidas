package co.juan.plazacomidas.api.dto.pedido;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoRequestDto {

    @NotNull(message = "El id del restaurante es obligatorio")
    private Long idRestaurante;

    @NotEmpty(message = "La lista de platos es obligatoria")
    private List<PlatoPedidoRequestDto> platos;
}
