package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.PedidoEntity;
import co.juan.plazacomidas.model.utils.EstadoPedido;
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
}
