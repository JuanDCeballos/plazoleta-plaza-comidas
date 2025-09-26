package co.juan.plazacomidas.api.dto.restaurante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteListadoDto {
    private String nombre;
    private String urlLogo;
}
