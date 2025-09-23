package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.PlatoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JpaPlatoRepository extends CrudRepository<PlatoEntity, Long>, QueryByExampleExecutor<PlatoEntity> {
}