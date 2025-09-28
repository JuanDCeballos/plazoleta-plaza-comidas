package co.juan.plazacomidas.api;

import co.juan.plazacomidas.api.dto.ApiResponse;
import co.juan.plazacomidas.api.dto.pedido.EntregarPedidoRequestDto;
import co.juan.plazacomidas.api.dto.pedido.PedidoRequestDto;
import co.juan.plazacomidas.api.dto.pedido.PedidoResponseDto;
import co.juan.plazacomidas.api.utils.PedidoMapper;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.model.utils.EstadoPedido;
import co.juan.plazacomidas.usecase.pedido.PedidoUseCase;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;
    private final PedidoMapper pedidoMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> crearPedido(
            @Valid @RequestBody PedidoRequestDto pedidoRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
        String emailCliente = userDetails.getUsername();

        Pedido pedido = pedidoMapper.toPedido(pedidoRequestDto);

        Pedido pedidoGuardado = pedidoUseCase.crearPedido(emailCliente, pedido);

        PedidoResponseDto responseDto = pedidoMapper.toPedidoResponseDto(pedidoGuardado);

        ApiResponse<PedidoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public ResponseEntity<ApiResponse<Pagina<PedidoResponseDto>>> listarPedidos(
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        Optional<EstadoPedido> estadoFiltro = Optional.ofNullable(estado)
                .map(String::toUpperCase)
                .map(EstadoPedido::valueOf);

        String emailEmpleado = userDetails.getUsername();

        Pagina<Pedido> paginaDominio = pedidoUseCase.listarPedidosPorEstado(
                emailEmpleado, estadoFiltro, page, size);

        List<PedidoResponseDto> contenido = paginaDominio.getContenido().stream()
                .map(pedidoMapper::toPedidoResponseDto)
                .toList();

        Pagina<PedidoResponseDto> paginaRespuesta = new Pagina<>(
                contenido,
                paginaDominio.getTotalElementos(),
                paginaDominio.getTotalPaginas(),
                paginaDominio.getNumeroPagina(),
                paginaDominio.getTamanoPagina()
        );

        return ResponseEntity.ok(new ApiResponse<>(paginaRespuesta));
    }

    @PatchMapping("/{idPedido}/asignar")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> asignarPedido(
            @PathVariable("idPedido") Long idPedido, @AuthenticationPrincipal UserDetails userDetails) {
        String emailEmpleado = userDetails.getUsername();

        Pedido pedidoActualizado = pedidoUseCase.asignarPedidoYCambiarEstado(emailEmpleado, idPedido);

        PedidoResponseDto responseDto = pedidoMapper.toPedidoResponseDto(pedidoActualizado);

        ApiResponse<PedidoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{idPedido}/listo")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> marcarPedidoComoListo(
            @PathVariable("idPedido") Long idPedido, @AuthenticationPrincipal UserDetails userDetails) {
        String emailEmpleado = userDetails.getUsername();

        Pedido pedidoActualizado = pedidoUseCase.marcarPedidoComoListo(emailEmpleado, idPedido);

        PedidoResponseDto responseDto = pedidoMapper.toPedidoResponseDto(pedidoActualizado);

        ApiResponse<PedidoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{idPedido}/entregar")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> entregarPedido(
            @PathVariable("idPedido") Long idPedido, @Valid @RequestBody EntregarPedidoRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String emailEmpleado = userDetails.getUsername();

        Pedido pedidoActualizado = pedidoUseCase.entregarPedido(emailEmpleado, idPedido, requestDto.getPinEntrega());

        PedidoResponseDto responseDto = pedidoMapper.toPedidoResponseDto(pedidoActualizado);

        ApiResponse<PedidoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{idPedido}/cancelar")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<ApiResponse<PedidoResponseDto>> cancelarPedido(
            @PathVariable("idPedido") Long idPedido, @AuthenticationPrincipal UserDetails userDetails) {
        String emailEmpleado = userDetails.getUsername();

        Pedido pedidoCancelado = pedidoUseCase.cancelarPedido(emailEmpleado, idPedido);

        PedidoResponseDto responseDto = pedidoMapper.toPedidoResponseDto(pedidoCancelado);

        ApiResponse<PedidoResponseDto> apiResponse = new ApiResponse<>(responseDto);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
