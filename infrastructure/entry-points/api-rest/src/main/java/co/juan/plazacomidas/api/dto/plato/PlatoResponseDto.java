package co.juan.plazacomidas.api.dto.plato;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatoResponseDto {

    private Long idPlato;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private String urlImagen;
    private Long categoria;
    private boolean activo;
    private Long idRestaurante;
}
