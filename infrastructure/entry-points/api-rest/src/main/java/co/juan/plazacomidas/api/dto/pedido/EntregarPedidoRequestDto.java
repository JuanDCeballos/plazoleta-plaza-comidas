package co.juan.plazacomidas.api.dto.pedido;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntregarPedidoRequestDto {

    @NotBlank(message = "El PIN de entrega es obligatorio")
    private String pinEntrega;
}
