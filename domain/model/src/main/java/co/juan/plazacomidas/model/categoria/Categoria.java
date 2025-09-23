package co.juan.plazacomidas.model.categoria;

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
public class Categoria {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
}
