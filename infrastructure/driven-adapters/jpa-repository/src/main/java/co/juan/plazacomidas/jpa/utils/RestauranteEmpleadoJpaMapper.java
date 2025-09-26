package co.juan.plazacomidas.jpa.utils;

import co.juan.plazacomidas.jpa.entities.RestauranteEmpleadoEntity;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestauranteEmpleadoJpaMapper {

    @Mapping(source = "restaurante.idRestaurante", target = "idRestaurante")
    RestauranteEmpleado toDomain(RestauranteEmpleadoEntity entity);

    @Mapping(target = "restaurante", ignore = true)
    RestauranteEmpleadoEntity toEntity(RestauranteEmpleado domain);
}
