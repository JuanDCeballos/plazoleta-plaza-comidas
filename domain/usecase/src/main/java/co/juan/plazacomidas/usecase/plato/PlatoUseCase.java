package co.juan.plazacomidas.usecase.plato;

import co.juan.plazacomidas.model.categoria.gateways.CategoriaRepository;
import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlatoUseCase {

    private final PlatoRepository platoRepository;
    private final RestauranteRepository restauranteRepository;
    private final CategoriaRepository categoriaRepository;

    public Plato crearPlato(Long idRestaurante, Plato plato) {

        if (!restauranteRepository.existePorId(idRestaurante)) {
            throw new ResourceNotFoundException("Restaurante no encontrado con el id: " + idRestaurante);
        }

        if (!categoriaRepository.existePorId(plato.getCategoria())) {
            throw new ResourceNotFoundException("Categoria no encontrada con el id: " + plato.getCategoria());
        }

        plato.setIdRestaurante(idRestaurante);
        plato.setActivo(true);

        return platoRepository.crearPlato(plato);
    }
}
