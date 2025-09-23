package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRestauranteRepositoryAdapter extends AdapterOperations<Restaurante, RestauranteEntity, Long, JpaRestauranteRepository>
        implements RestauranteRepository {

    public JpaRestauranteRepositoryAdapter(JpaRestauranteRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Restaurante.class));
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
}
