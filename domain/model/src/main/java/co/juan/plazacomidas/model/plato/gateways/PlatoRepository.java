package co.juan.plazacomidas.model.plato.gateways;

import co.juan.plazacomidas.model.plato.Plato;

import java.util.Optional;

public interface PlatoRepository {

    Plato guardarPlato(Plato plato);

    Optional<Plato> buscarPorId(Long idPlato);
}
