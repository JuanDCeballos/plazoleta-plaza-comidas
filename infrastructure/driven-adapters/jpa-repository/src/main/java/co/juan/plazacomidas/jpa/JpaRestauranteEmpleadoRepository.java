package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.RestauranteEmpleadoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

public interface JpaRestauranteEmpleadoRepository extends CrudRepository<RestauranteEmpleadoEntity, Long>, QueryByExampleExecutor<RestauranteEmpleadoEntity> {

    Optional<RestauranteEmpleadoEntity> findByIdUsuarioEmpleado(Long idUsuarioEmpleado);
}