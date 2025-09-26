package co.juan.plazacomidas.jpa.utils;

import co.juan.plazacomidas.jpa.dto.RestauranteListadoDto;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestauranteJpaMapper {

    Restaurante toListadoRestaurante(RestauranteListadoDto listadoDto);
}
