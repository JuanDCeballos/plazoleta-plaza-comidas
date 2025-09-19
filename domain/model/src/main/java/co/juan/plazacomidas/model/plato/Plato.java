package co.juan.plazacomidas.model.plato;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Plato {

    private Long idPlato;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private String urlImagen;
    private Long categoria; //TODO: REVISAR COMO SE VAN A MANEJAR ESTAS CATEGORIAS
}
