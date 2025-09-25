package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.RestauranteEmpleadoEntity;
import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.jpa.utils.RestauranteEmpleadoJpaMapper;
import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.model.restauranteempleado.gateways.RestauranteEmpleadoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaRestauranteEmpleadoRepositoryAdapter extends AdapterOperations<RestauranteEmpleado, RestauranteEmpleadoEntity, Long, JpaRestauranteEmpleadoRepository>
        implements RestauranteEmpleadoRepository {

    private final RestauranteEmpleadoJpaMapper restauranteEmpleadoJpaMapper;
    private final JpaRestauranteRepository jpaRestauranteRepository;

    public JpaRestauranteEmpleadoRepositoryAdapter(JpaRestauranteEmpleadoRepository repository, ObjectMapper mapper,
                                                   JpaRestauranteRepository jpaRestauranteRepository,
                                                   RestauranteEmpleadoJpaMapper restauranteEmpleadoJpaMapper) {
        super(repository, mapper, d -> mapper.map(d, RestauranteEmpleado.class));
        this.jpaRestauranteRepository = jpaRestauranteRepository;
        this.restauranteEmpleadoJpaMapper = restauranteEmpleadoJpaMapper;
    }

    @Override
    public RestauranteEmpleado guardar(RestauranteEmpleado restauranteEmpleado) {
        RestauranteEmpleadoEntity restauranteEmpleadoEntity = restauranteEmpleadoJpaMapper.toEntity(restauranteEmpleado);

        RestauranteEntity restauranteEntity = jpaRestauranteRepository.findById(restauranteEmpleado.getIdRestaurante())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Restaurante no encontrado en la base de datos con el id: " +
                                restauranteEmpleado.getIdRestaurante()));
        restauranteEmpleadoEntity.setRestaurante(restauranteEntity);

        RestauranteEmpleadoEntity restauranteEmpleadoGuardado = repository.save(restauranteEmpleadoEntity);

        return restauranteEmpleadoJpaMapper.toDomain(restauranteEmpleadoGuardado);
    }

    @Override
    public Optional<RestauranteEmpleado> buscarByIdUsuarioEmpleado(Long idUsuarioEmpleado) {
        return repository.findByIdUsuarioEmpleado(idUsuarioEmpleado)
                .map(restauranteEmpleadoJpaMapper::toDomain);
    }
}
