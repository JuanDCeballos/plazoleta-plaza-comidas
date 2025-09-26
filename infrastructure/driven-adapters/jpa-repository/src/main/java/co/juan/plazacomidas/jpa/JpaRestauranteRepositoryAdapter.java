package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.dto.RestauranteListadoDto;
import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.jpa.utils.RestauranteJpaMapper;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaRestauranteRepositoryAdapter extends AdapterOperations<Restaurante, RestauranteEntity, Long, JpaRestauranteRepository>
        implements RestauranteRepository {

    private final RestauranteJpaMapper restauranteJpaMapper;

    public JpaRestauranteRepositoryAdapter(JpaRestauranteRepository repository, ObjectMapper mapper,
                                           RestauranteJpaMapper restauranteJpaMapper) {
        super(repository, mapper, d -> mapper.map(d, Restaurante.class));
        this.restauranteJpaMapper = restauranteJpaMapper;
    }

    @Override
    public Restaurante crearRestaurante(Restaurante restaurante) {
        return save(restaurante);
    }

    @Override
    public boolean existePorId(Long idRestaurante) {
        return repository.existsById(idRestaurante);
    }

    @Override
    public Restaurante obtenerById(Long idRestaurante) {
        return findById(idRestaurante);
    }

    @Override
    public Pagina<Restaurante> listarRestaurantesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());

        Page<RestauranteListadoDto> customPage = repository.findAllRestaurantesPaginado(pageable);

        List<Restaurante> contenidoDominio = customPage.getContent().stream()
                .map(restauranteJpaMapper::toListadoRestaurante)
                .toList();

        return Pagina.<Restaurante>builder()
                .contenido(contenidoDominio)
                .totalElementos(customPage.getTotalElements())
                .totalPaginas(customPage.getTotalPages())
                .numeroPagina(customPage.getNumber())
                .tamanoPagina(customPage.getSize())
                .build();
    }
}
