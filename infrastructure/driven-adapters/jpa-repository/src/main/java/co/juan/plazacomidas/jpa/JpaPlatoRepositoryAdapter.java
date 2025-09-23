package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.CategoriaEntity;
import co.juan.plazacomidas.jpa.entities.PlatoEntity;
import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.jpa.utils.PlatoEntityMapper;
import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaPlatoRepositoryAdapter extends AdapterOperations<Plato, PlatoEntity, Long, JpaPlatoRepository>
        implements PlatoRepository {

    private final JpaRestauranteRepository jpaRestauranteRepository;
    private final JpaCategoriaRepository jpaCategoriaRepository;
    private final PlatoEntityMapper platoEntityMapper;

    public JpaPlatoRepositoryAdapter(JpaPlatoRepository repository, ObjectMapper mapper,
                                     JpaRestauranteRepository jpaRestauranteRepository, PlatoEntityMapper platoEntityMapper,
                                     JpaCategoriaRepository jpaCategoriaRepository) {
        super(repository, mapper, d -> mapper.map(d, Plato.class));
        this.jpaRestauranteRepository = jpaRestauranteRepository;
        this.jpaCategoriaRepository = jpaCategoriaRepository;
        this.platoEntityMapper = platoEntityMapper;
    }

    @Override
    public Plato guardarPlato(Plato plato) {
        PlatoEntity platoEntity = platoEntityMapper.toEntity(plato);

        RestauranteEntity restauranteEntity = jpaRestauranteRepository.findById(plato.getIdRestaurante())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado en la base de datos con el id: " + plato.getIdRestaurante()));
        platoEntity.setRestaurante(restauranteEntity);

        CategoriaEntity categoriaEntity = jpaCategoriaRepository.findById(plato.getCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada en la base de datos con el id: " + plato.getCategoria()));
        platoEntity.setCategoria(categoriaEntity);

        PlatoEntity platoGuardado = repository.save(platoEntity);

        return platoEntityMapper.toDomain(platoGuardado);
    }

    @Override
    public Optional<Plato> buscarPorId(Long idPlato) {
        return repository.findById(idPlato)
                .map(platoEntityMapper::toDomain);
    }
}
