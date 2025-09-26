package co.juan.plazacomidas.api.utils;

import co.juan.plazacomidas.api.dto.pedido.PedidoRequestDto;
import co.juan.plazacomidas.api.dto.pedido.PedidoResponseDto;
import co.juan.plazacomidas.model.pedido.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    Pedido toPedido(PedidoRequestDto requestDto);

    @Mapping(source = "id", target = "idPedido")
    PedidoResponseDto toPedidoResponseDto(Pedido pedido);
}
