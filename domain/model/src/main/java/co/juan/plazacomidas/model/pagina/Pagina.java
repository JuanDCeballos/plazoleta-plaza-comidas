package co.juan.plazacomidas.model.pagina;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Pagina<T> {

    private List<T> contenido;
    private long totalElementos;
    private int totalPaginas;
    private int numeroPagina;
    private int tamanoPagina;
}
