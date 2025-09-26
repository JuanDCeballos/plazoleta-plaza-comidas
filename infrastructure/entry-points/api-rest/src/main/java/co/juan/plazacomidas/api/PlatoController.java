package co.juan.plazacomidas.api;

import co.juan.plazacomidas.api.dto.ApiResponse;
import co.juan.plazacomidas.api.dto.plato.*;
import co.juan.plazacomidas.api.utils.PlatoMapper;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.usecase.plato.PlatoUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PlatoController {

    private final PlatoUseCase platoUseCase;
    private final PlatoMapper platoMapper;

    @PostMapping("/restaurantes/{idRestaurante}/platos")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<ApiResponse<PlatoResponseDto>> crearPlato(@PathVariable("idRestaurante") Long idRestaurante,
                                                                    @Valid @RequestBody PlatoRequestDto requestDto) {
        Plato plato = platoMapper.toPlato(requestDto);

        Plato platoGuardado = platoUseCase.crearPlato(idRestaurante, plato);

        PlatoResponseDto responseDto = platoMapper.toPlatoResponseDto(platoGuardado);

        ApiResponse<PlatoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/restaurantes/{idRestaurante}/platos/{idPlato}")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<ApiResponse<PlatoResponseDto>> actualizarPlato(@PathVariable("idRestaurante") Long idRestaurante,
                                                                         @PathVariable("idPlato") Long idPlato,
                                                                         @Valid @RequestBody ModificarPlatoRequestDto requestDto) {
        Plato plato = platoMapper.toPlato(requestDto);

        Plato platoActualizado = platoUseCase.actualizarPlato(idRestaurante, idPlato, plato);

        PlatoResponseDto responseDto = platoMapper.toPlatoResponseDto(platoActualizado);

        ApiResponse<PlatoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/restaurantes/{idRestaurante}/platos/{idPlato}/estado")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<ApiResponse<PlatoResponseDto>> actualizarEstadoPlato(@PathVariable("idRestaurante") Long idRestaurante,
                                                                               @PathVariable("idPlato") Long idPlato,
                                                                               @Valid @RequestBody ActualizarEstadoPlatoDto requestDto) {

        Plato platoActualizado = platoUseCase.actualizarEstadoPlato(idRestaurante, idPlato, requestDto.getActivo());

        PlatoResponseDto responseDto = platoMapper.toPlatoResponseDto(platoActualizado);

        ApiResponse<PlatoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/restaurantes/{idRestaurante}/platos")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<ApiResponse<Pagina<PlatoMenuDto>>> listarMenu(
            @PathVariable("idRestaurante") Long idRestaurante,
            @RequestParam(value = "idCategoria", required = false) Long idCategoria,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pagina<Plato> paginaDeDominio = platoUseCase.listarPlatosDeRestaurante(
                idRestaurante,
                Optional.ofNullable(idCategoria),
                page,
                size);

        List<PlatoMenuDto> contenidoDto = paginaDeDominio.getContenido().stream()
                .map(platoMapper::toPlatoMenuDto)
                .toList();

        Pagina<PlatoMenuDto> paginaDeRespuesta = new Pagina<>(
                contenidoDto,
                paginaDeDominio.getTotalElementos(),
                paginaDeDominio.getTotalPaginas(),
                paginaDeDominio.getNumeroPagina(),
                paginaDeDominio.getTamanoPagina()
        );

        return ResponseEntity.ok(new ApiResponse<>(paginaDeRespuesta));
    }
}
