package co.juan.plazacomidas.model.plato.gateways;

import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.plato.Plato;

import java.util.Optional;

public interface PlatoRepository {

    Plato guardarPlato(Plato plato);

    Optional<Plato> buscarPorId(Long idPlato);

    Pagina<Plato> listarPlatosPorRestaurante(Long idRestaurante, Optional<Long> idCategoria, int page, int size);
}
