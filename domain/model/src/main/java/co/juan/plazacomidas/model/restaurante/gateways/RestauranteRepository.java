package co.juan.plazacomidas.model.restaurante.gateways;

import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.restaurante.Restaurante;

public interface RestauranteRepository {

    Restaurante crearRestaurante(Restaurante restaurante);

    boolean existePorId(Long idRestaurante);

    Restaurante obtenerById(Long idRestaurante);

    Pagina<Restaurante> listarRestaurantesPaginados(int page, int size);
}
