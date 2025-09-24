package co.juan.plazacomidas.api;

import co.juan.plazacomidas.api.dto.ApiResponse;
import co.juan.plazacomidas.api.dto.restaurante.RestauranteListadoDto;
import co.juan.plazacomidas.api.dto.restaurante.RestauranteRequestDto;
import co.juan.plazacomidas.api.dto.restaurante.RestauranteResponseDto;
import co.juan.plazacomidas.api.dto.retauranteempleado.RestauranteEmpleadoRequestDto;
import co.juan.plazacomidas.api.dto.retauranteempleado.RestauranteEmpleadoResponseDto;
import co.juan.plazacomidas.api.utils.RestauranteEmpleadoMapper;
import co.juan.plazacomidas.api.utils.RestauranteMapper;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.usecase.restaurante.RestauranteUseCase;
import co.juan.plazacomidas.usecase.restauranteempleado.RestauranteEmpleadoUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestauranteController {

    private final RestauranteUseCase restauranteUseCase;
    private final RestauranteEmpleadoUseCase restauranteEmpleadoUseCase;
    private final RestauranteMapper restauranteMapper;
    private final RestauranteEmpleadoMapper restauranteEmpleadoMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<RestauranteResponseDto>> crearRestaurante(
            @Valid @RequestBody RestauranteRequestDto requestDto) {
        Restaurante restaurante = restauranteMapper.toRestaurante(requestDto);

        Restaurante restauranteGuardado = restauranteUseCase.crearRestaurante(restaurante);

        RestauranteResponseDto responseDto = restauranteMapper.toRestauranteResponseDto(restauranteGuardado);

        ApiResponse<RestauranteResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/{idRestaurante}/empleados")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<ApiResponse<RestauranteEmpleadoResponseDto>> crearRestauranteEmpleado(
            @PathVariable("idRestaurante") Long idRestaurante,
            @Valid @RequestBody RestauranteEmpleadoRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String emailPropietarioLogueado = userDetails.getUsername();

        RestauranteEmpleado restauranteEmpleado = restauranteEmpleadoMapper.toRestauranteEmpleado(requestDto);

        RestauranteEmpleado restauranteEmpleadoGuardado = restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                emailPropietarioLogueado, idRestaurante, restauranteEmpleado);

        RestauranteEmpleadoResponseDto responseDto = restauranteEmpleadoMapper.toRestauranteEmpleadoResponseDto(
                restauranteEmpleadoGuardado);

        ApiResponse<RestauranteEmpleadoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<ApiResponse<Pagina<RestauranteListadoDto>>> listarRestaurantes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pagina<Restaurante> paginaDeDominio = restauranteUseCase.listarRestaurantes(page, size);

        List<RestauranteListadoDto> contenidoDto = paginaDeDominio.getContenido().stream()
                .map(restauranteMapper::toListadoRestaurante)
                .toList();

        Pagina<RestauranteListadoDto> paginaDeRespuesta = Pagina.<RestauranteListadoDto>builder()
                .contenido(contenidoDto)
                .totalElementos(paginaDeDominio.getTotalElementos())
                .totalPaginas(paginaDeDominio.getTotalPaginas())
                .numeroPagina(paginaDeDominio.getNumeroPagina())
                .tamanoPagina(paginaDeDominio.getTamanoPagina())
                .build();

        return ResponseEntity.ok(new ApiResponse<>(paginaDeRespuesta));
    }
}
