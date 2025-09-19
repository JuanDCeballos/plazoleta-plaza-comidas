package co.juan.plazacomidas.api;

import co.juan.plazacomidas.api.dto.ApiResponse;
import co.juan.plazacomidas.api.dto.plato.PlatoRequestDto;
import co.juan.plazacomidas.api.dto.plato.PlatoResponseDto;
import co.juan.plazacomidas.api.utils.PlatoMapper;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.usecase.plato.PlatoUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PlatoController {

    private final PlatoUseCase platoUseCase;
    private final PlatoMapper platoMapper;

    @PostMapping("/restaurantes/{idRestaurante}/platos")
    public ResponseEntity<ApiResponse<PlatoResponseDto>> crearPlato(@PathVariable("idRestaurante") Long idRestaurante, @Valid @RequestBody PlatoRequestDto requestDto) {
        Plato plato = platoMapper.toPlato(requestDto);

        Plato platoGuardado = platoUseCase.crearPlato(idRestaurante, plato);

        PlatoResponseDto responseDto = platoMapper.toPlatoResponseDto(platoGuardado);

        ApiResponse<PlatoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
