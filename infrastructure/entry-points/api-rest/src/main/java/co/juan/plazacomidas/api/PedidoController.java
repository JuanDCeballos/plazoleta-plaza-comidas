package co.juan.plazacomidas.api;

import co.juan.plazacomidas.api.dto.ApiResponse;
import co.juan.plazacomidas.api.dto.pedido.PedidoRequestDto;
import co.juan.plazacomidas.api.dto.pedido.PedidoResponseDto;
import co.juan.plazacomidas.api.utils.PedidoMapper;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.usecase.pedido.PedidoUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
