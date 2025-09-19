package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.CategoriaEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.model.categoria.Categoria;
import co.juan.plazacomidas.model.categoria.gateways.CategoriaRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JpaCategoriaRepositoryAdapter extends AdapterOperations<Categoria, CategoriaEntity, Long, JpaCategoriaRepository>
        implements CategoriaRepository {

    public JpaCategoriaRepositoryAdapter(JpaCategoriaRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Categoria.class));
    }

    @Override
    public boolean existePorId(Long idCategoria) {
        return repository.existsById(idCategoria);
    }
}
