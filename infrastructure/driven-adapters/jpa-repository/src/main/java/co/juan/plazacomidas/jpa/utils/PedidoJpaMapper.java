package co.juan.plazacomidas.jpa.utils;

import co.juan.plazacomidas.jpa.entities.PedidoEntity;
import co.juan.plazacomidas.jpa.entities.PedidoPlatoEntity;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.model.pedidoplato.PedidoPlato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoJpaMapper {

    @Mapping(source = "restaurante.idRestaurante", target = "idRestaurante")
    @Mapping(source = "pedidoPlatos", target = "platos")
    Pedido toDomain(PedidoEntity entity);

    @Mapping(target = "restaurante", ignore = true)
    @Mapping(target = "pedidoPlatos", ignore = true)
    PedidoEntity toEntity(Pedido pedido);

    @Mapping(source = "plato.idPlato", target = "idPlato")
    PedidoPlato toPedidoPlatoDomain(PedidoPlatoEntity pedidoPlatoEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "plato", ignore = true)
    PedidoPlatoEntity toPedidoPlatoEntity(PedidoPlato pedidoPlato);
}
