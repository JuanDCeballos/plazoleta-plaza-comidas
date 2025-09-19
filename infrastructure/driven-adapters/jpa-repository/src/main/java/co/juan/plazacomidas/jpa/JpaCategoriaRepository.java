package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.CategoriaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JpaCategoriaRepository extends CrudRepository<CategoriaEntity, Long>, QueryByExampleExecutor<CategoriaEntity> {
}