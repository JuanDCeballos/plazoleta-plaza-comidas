package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.PlatoEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JpaPlatoRepositoryAdapter extends AdapterOperations<Plato, PlatoEntity, Long, JpaPlatoRepository>
        implements PlatoRepository {

    public JpaPlatoRepositoryAdapter(JpaPlatoRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Plato.class));
    }

    @Override
    public Plato crearPlato(Plato plato) {
        return null;
    }
}
