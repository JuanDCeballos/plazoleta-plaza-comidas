package co.juan.plazacomidas.api.utils;

import co.juan.plazacomidas.api.dto.plato.PlatoRequestDto;
import co.juan.plazacomidas.api.dto.plato.PlatoResponseDto;
import co.juan.plazacomidas.model.plato.Plato;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatoMapper {

    Plato toPlato(PlatoRequestDto requestDto);

    PlatoResponseDto toPlatoResponseDto(Plato plato);
}
