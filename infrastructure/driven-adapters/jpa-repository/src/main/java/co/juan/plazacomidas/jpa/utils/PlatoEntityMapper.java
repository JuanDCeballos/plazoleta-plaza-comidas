package co.juan.plazacomidas.jpa.utils;

import co.juan.plazacomidas.jpa.entities.PlatoEntity;
import co.juan.plazacomidas.model.plato.Plato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlatoEntityMapper {

    @Mapping(source = "restaurante.idRestaurante", target = "idRestaurante")
    @Mapping(source = "categoria.idCategoria", target = "categoria")
    Plato toDomain(PlatoEntity entity);

    @Mapping(target = "restaurante", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    PlatoEntity toEntity(Plato domain);
}
