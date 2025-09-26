package co.juan.plazacomidas.api.dto.pedido;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDto {
    private Long idPedido;
    private Long idCliente;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private String estado;
    private Long idChef;
    private Long idRestaurante;
    private List<PedidoPlatoResponseDto> platos;
}
