package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.PlatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JpaPlatoRepository extends CrudRepository<PlatoEntity, Long>, QueryByExampleExecutor<PlatoEntity> {

    @Query("SELECT p FROM PlatoEntity p WHERE p.restaurante.idRestaurante = :idRestaurante " +
            "AND (:idCategoria IS NULL OR p.categoria.idCategoria = :idCategoria) " +
            "AND p.activo = true")
    Page<PlatoEntity> findByRestauranteAndCategoriaOptional(
            @Param("idRestaurante") Long idRestaurante,
            @Param("idCategoria") Long idCategoria,
            Pageable pageable);
}