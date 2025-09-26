package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.PedidoEntity;
import co.juan.plazacomidas.model.utils.EstadoPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface JpaPedidoRepository extends CrudRepository<PedidoEntity, Long>, QueryByExampleExecutor<PedidoEntity> {

    @Query("SELECT EXISTS(" +
            "SELECT 1 " +
            "FROM PedidoEntity p " +
            "WHERE p.idCliente = :idCliente " +
            "AND p.estado IN :estados " +
            ") AS resultado")
    boolean clienteTienePedidoActivo(@Param("idCliente") Long idCliente, @Param("estados") List<EstadoPedido> estados);

    @Query("SELECT p FROM PedidoEntity p " +
            "WHERE p.restaurante.idRestaurante = :idRestaurante " +
            "AND (:estado IS NULL OR p.estado = :estado)")
    Page<PedidoEntity> findByRestauranteAndEstadoOptional(
            @Param("idRestaurante") Long idRestaurante,
            @Param("estado") EstadoPedido estado,
            Pageable pageable);
}
