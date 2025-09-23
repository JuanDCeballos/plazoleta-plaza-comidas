package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JpaRestauranteRepository extends CrudRepository<RestauranteEntity, Long>, QueryByExampleExecutor<RestauranteEntity> {
}
