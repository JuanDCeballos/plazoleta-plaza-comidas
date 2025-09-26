package co.juan.plazacomidas.api.utils;

import co.juan.plazacomidas.api.dto.retauranteempleado.RestauranteEmpleadoRequestDto;
import co.juan.plazacomidas.api.dto.retauranteempleado.RestauranteEmpleadoResponseDto;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestauranteEmpleadoMapper {

    RestauranteEmpleado toRestauranteEmpleado(RestauranteEmpleadoRequestDto requestDto);

    RestauranteEmpleadoResponseDto toRestauranteEmpleadoResponseDto(RestauranteEmpleado restauranteEmpleado);
}
