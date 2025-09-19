package co.juan.plazacomidas.api;

import co.juan.plazacomidas.api.dto.ApiResponse;
import co.juan.plazacomidas.api.dto.restaurante.RestauranteRequestDto;
import co.juan.plazacomidas.api.dto.restaurante.RestauranteResponseDto;
import co.juan.plazacomidas.api.utils.RestauranteMapper;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.usecase.restaurante.RestauranteUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestauranteController {

    private final RestauranteUseCase restauranteUseCase;
    private final RestauranteMapper restauranteMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<RestauranteResponseDto>> crearRestaurante(@Valid @RequestBody RestauranteRequestDto requestDto) {
        Restaurante restaurante = restauranteMapper.toRestaurante(requestDto);

        Restaurante restauranteGuardado = restauranteUseCase.crearRestaurante(restaurante);

        RestauranteResponseDto responseDto = restauranteMapper.toRestauranteResponseDto(restauranteGuardado);

        ApiResponse<RestauranteResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
