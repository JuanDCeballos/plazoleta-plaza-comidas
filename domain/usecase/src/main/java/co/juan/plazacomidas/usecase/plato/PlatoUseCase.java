package co.juan.plazacomidas.usecase.plato;

import co.juan.plazacomidas.model.categoria.gateways.CategoriaRepository;
import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.utils.MensajesEnum;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PlatoUseCase {

    private final PlatoRepository platoRepository;
    private final RestauranteRepository restauranteRepository;
    private final CategoriaRepository categoriaRepository;

    public Plato crearPlato(Long idRestaurante, Plato plato) {
        validarExistenciaRestaurante(idRestaurante);

        if (!categoriaRepository.existePorId(plato.getCategoria())) {
            throw new ResourceNotFoundException(
                    MensajesEnum.CATEGORIA_NO_ENCONTRADA_POR_ID.getMensaje() + plato.getCategoria());
        }

        plato.setIdRestaurante(idRestaurante);
        plato.setActivo(true);

        return platoRepository.guardarPlato(plato);
    }

    public Plato actualizarPlato(Long idRestaurante, Long idPlato, Plato platoConNuevosDatos) {
        validarExistenciaRestaurante(idRestaurante);
        Plato platoExistente = obtenerYValidarPlatoRestaurante(idRestaurante, idPlato);

        if (platoConNuevosDatos.getPrecio() != null) {
            platoExistente.setPrecio(platoConNuevosDatos.getPrecio());
        }

        if (platoConNuevosDatos.getDescripcion() != null) {
            platoExistente.setDescripcion(platoConNuevosDatos.getDescripcion());
        }

        return platoRepository.guardarPlato(platoExistente);
    }

    public Plato actualizarEstadoPlato(Long idRestaurante, Long idPlato, boolean estado) {
        validarExistenciaRestaurante(idRestaurante);
        Plato platoExistente = obtenerYValidarPlatoRestaurante(idRestaurante, idPlato);

        platoExistente.setActivo(estado);

        return platoRepository.guardarPlato(platoExistente);
    }

    public Pagina<Plato> listarPlatosDeRestaurante(Long idRestaurante, Optional<Long> idCategoria, int page, int size) {
        validarExistenciaRestaurante(idRestaurante);

        return platoRepository.listarPlatosPorRestaurante(idRestaurante, idCategoria, page, size);
    }

    private void validarExistenciaRestaurante(Long idRestaurante) {
        if (!restauranteRepository.existePorId(idRestaurante)) {
            throw new ResourceNotFoundException(
                    MensajesEnum.RESTAURANTE_NO_ENCONTRADO.getMensaje() + idRestaurante);
        }
    }

    private Plato obtenerYValidarPlatoRestaurante(Long idRestaurante, Long idPlato) {
        Plato plato = platoRepository.buscarPorId(idPlato)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.PLATO_NO_ENCONTRADO.getMensaje() + idPlato));

        if (!plato.getIdRestaurante().equals(idRestaurante)) {
            throw new IllegalArgumentException(MensajesEnum.PLATO.getMensaje() + plato.getNombre() +
                    MensajesEnum.NO_PERTECENE_A_RESTAURANTE.getMensaje());
        }

        return plato;
    }
}
