package co.juan.plazacomidas.api.utils;

import co.juan.plazacomidas.api.dto.restaurante.RestauranteRequestDto;
import co.juan.plazacomidas.api.dto.restaurante.RestauranteResponseDto;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {

    Restaurante toRestaurante(RestauranteRequestDto requestDto);

    RestauranteResponseDto toRestauranteResponseDto(Restaurante restaurante);
}
