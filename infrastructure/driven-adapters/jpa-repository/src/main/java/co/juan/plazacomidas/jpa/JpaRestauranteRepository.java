package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.dto.RestauranteListadoDto;
import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JpaRestauranteRepository extends CrudRepository<RestauranteEntity, Long>, QueryByExampleExecutor<RestauranteEntity> {

    @Query("SELECT new co.juan.plazacomidas.jpa.dto.RestauranteListadoDto(r.nombre, r.urlLogo) FROM RestauranteEntity r")
    Page<RestauranteListadoDto> findAllRestaurantesPaginado(Pageable pageable);
}
