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

        return platoRepository.guardarPlato(plato);
    }

    public Plato actualizarPlato(Long idRestaurante, Long idPlato, Plato platoConNuevosDatos) {
        if (!restauranteRepository.existePorId(idRestaurante)) {
            throw new ResourceNotFoundException("Restaurante no encontrado con el id: " + idRestaurante);
        }

        Plato platoExistente = platoRepository.buscarPorId(idPlato)
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con el id: " + idPlato));

        if (!platoExistente.getIdRestaurante().equals(idRestaurante)) {
            throw new IllegalArgumentException("El plato no pertenece al restaurante especificado.");
        }

        if (platoConNuevosDatos.getPrecio() != null) {
            platoExistente.setPrecio(platoConNuevosDatos.getPrecio());
        }

        if (platoConNuevosDatos.getDescripcion() != null) {
            platoExistente.setDescripcion(platoConNuevosDatos.getDescripcion());
        }

        return platoRepository.guardarPlato(platoExistente);
    }
}
