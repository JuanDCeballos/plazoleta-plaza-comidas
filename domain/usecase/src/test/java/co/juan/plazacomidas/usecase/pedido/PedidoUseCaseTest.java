package co.juan.plazacomidas.usecase.pedido;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.model.pedido.gateways.PedidoRepository;
import co.juan.plazacomidas.model.pedidoplato.PedidoPlato;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.model.restauranteempleado.gateways.RestauranteEmpleadoRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import co.juan.plazacomidas.model.utils.EstadoPedido;
import co.juan.plazacomidas.model.utils.MensajesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoUseCaseTest {

    @InjectMocks
    PedidoUseCase pedidoUseCase;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    RestauranteRepository restauranteRepository;

    @Mock
    PlatoRepository platoRepository;

    @Mock
    UsuarioGateway usuarioGateway;

    @Mock
    RestauranteEmpleadoRepository restauranteEmpleadoRepository;

    private Pedido pedido;
    private Usuario usuario;
    private Plato plato;
    private RestauranteEmpleado restauranteEmpleado;
    private Pagina<Pedido> pedidosPaginadosYFiltrados;

    private final String emailCliente = "juan.ceballos@correo.com.co";
    private final Long idPedido = 1L;

    @BeforeEach
    void initMocks() {
        List<PedidoPlato> pedidoPlatoLst = new ArrayList<>();
        PedidoPlato pedidoPlato = new PedidoPlato();
        pedidoPlato.setIdPlato(1L);
        pedidoPlato.setCantidad(3);
        pedidoPlatoLst.add(pedidoPlato);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setIdCliente(1L);
        pedido.setFecha(LocalDate.of(2025, 9, 24));
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setIdChef(1L);
        pedido.setIdRestaurante(1L);
        pedido.setPlatos(pedidoPlatoLst);

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Ceballos");
        usuario.setDocumentoDeIdentidad(2847564329L);
        usuario.setCelular("8765436278");
        usuario.setFechaNacimiento(LocalDate.of(2002, 9, 24));
        usuario.setCorreo("juan.ceballos@correo.com.co");
        usuario.setIdRol(1L);

        plato = new Plato();
        plato.setIdPlato(1L);
        plato.setNombre("Hamburguesa");
        plato.setPrecio(new BigDecimal("35000"));
        plato.setDescripcion("Pan, carne, salsa, lechuga, tomate");
        plato.setUrlImagen("https://url.com");
        plato.setCategoria(1L);
        plato.setActivo(true);
        plato.setIdRestaurante(1L);

        restauranteEmpleado = new RestauranteEmpleado();
        restauranteEmpleado.setId(1L);
        restauranteEmpleado.setIdRestaurante(1L);
        restauranteEmpleado.setIdUsuarioEmpleado(1L);

        pedidosPaginadosYFiltrados = new Pagina<>();
        pedidosPaginadosYFiltrados.setContenido(List.of(pedido));
        pedidosPaginadosYFiltrados.setTotalElementos(1L);
        pedidosPaginadosYFiltrados.setTotalPaginas(2);
        pedidosPaginadosYFiltrados.setNumeroPagina(0);
        pedidosPaginadosYFiltrados.setTamanoPagina(5);
    }

    @Test
    void crearPedido() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(pedidoRepository.clienteTienePedidoActivo(anyLong())).thenReturn(false);
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));
        when(pedidoRepository.guardarPedido(any(Pedido.class))).thenReturn(pedido);

        Pedido pedidoGuardado = pedidoUseCase.crearPedido(emailCliente, pedido);
        assertNotNull(pedidoGuardado);
        assertEquals(EstadoPedido.PENDIENTE, pedidoGuardado.getEstado());
        assertEquals(1, pedidoGuardado.getPlatos().size());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(pedidoRepository, times(1)).clienteTienePedidoActivo(anyLong());
        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(pedidoRepository, times(1)).guardarPedido(any(Pedido.class));
    }

    @Test
    void crearPedido_retornaException_cuandoNoExisteUsuarioPorCorreo() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                pedidoUseCase.crearPedido(emailCliente, pedido)
        );
        assertEquals("Usuario no encontrado con el email: " + emailCliente, exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(pedidoRepository, times(0)).clienteTienePedidoActivo(anyLong());
        verify(restauranteRepository, times(0)).existePorId(anyLong());
        verify(platoRepository, times(0)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).guardarPedido(any(Pedido.class));
    }

    @Test
    void crearPedido_retornaException_clienteTienePedidoActivo() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(pedidoRepository.clienteTienePedidoActivo(anyLong())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                pedidoUseCase.crearPedido(emailCliente, pedido)
        );
        assertEquals("No puede crear un nuevo pedido mientras tenga uno en proceso.", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(pedidoRepository, times(1)).clienteTienePedidoActivo(anyLong());
        verify(restauranteRepository, times(0)).existePorId(anyLong());
        verify(platoRepository, times(0)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).guardarPedido(any(Pedido.class));
    }

    @Test
    void crearPedido_retornaException_cuandoNoExisteRestaurante() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(pedidoRepository.clienteTienePedidoActivo(anyLong())).thenReturn(false);
        when(restauranteRepository.existePorId(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                pedidoUseCase.crearPedido(emailCliente, pedido)
        );
        assertEquals("Restaurante no encontrado con el id: " + pedido.getIdRestaurante(), exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(pedidoRepository, times(1)).clienteTienePedidoActivo(anyLong());
        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(0)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).guardarPedido(any(Pedido.class));
    }

    @Test
    void crearPedido_retornaException_cuandoPlatoNoExistePlato() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(pedidoRepository.clienteTienePedidoActivo(anyLong())).thenReturn(false);
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                pedidoUseCase.crearPedido(emailCliente, pedido)
        );
        assertEquals("Plato no encontrado con el id: " + plato.getIdPlato(), exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(pedidoRepository, times(1)).clienteTienePedidoActivo(anyLong());
        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).guardarPedido(any(Pedido.class));
    }

    @Test
    void crearPedido_retornaException_cuandoPlatoNoPerteneceARestaurante() {
        plato.setIdRestaurante(5L);

        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(pedidoRepository.clienteTienePedidoActivo(anyLong())).thenReturn(false);
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                pedidoUseCase.crearPedido(emailCliente, pedido)
        );
        assertEquals("El plato " + plato.getNombre() + " no pertenece a este restaurante.", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(pedidoRepository, times(1)).clienteTienePedidoActivo(anyLong());
        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).guardarPedido(any(Pedido.class));
    }

    @Test
    void listarPedidosPorEstado() {
        int size = 5;
        int page = 0;

        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(anyLong()))
                .thenReturn(Optional.of(restauranteEmpleado));
        when(pedidoRepository.listarPedidosPorRestauranteYEstado(
                anyLong(), any(), anyInt(), anyInt())).thenReturn(pedidosPaginadosYFiltrados);

        Pagina<Pedido> pedidosPagina = pedidoUseCase.listarPedidosPorEstado(
                emailCliente, Optional.of(EstadoPedido.PENDIENTE), page, size);
        assertNotNull(pedidosPagina);
        assertEquals(EstadoPedido.PENDIENTE, pedidosPagina.getContenido().getFirst().getEstado());
        assertEquals(1L, pedidosPagina.getTotalElementos());
        assertEquals(2, pedidosPagina.getTotalPaginas());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteEmpleadoRepository, times(1)).buscarByIdUsuarioEmpleado(anyLong());
        verify(pedidoRepository, times(1)).listarPedidosPorRestauranteYEstado(
                anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void asignarPedidoYCambiarEstado() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(anyLong()))
                .thenReturn(Optional.of(restauranteEmpleado));
        when(pedidoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(pedido));
        when(pedidoRepository.actualizarPedido(any(Pedido.class))).thenReturn(pedido);

        Pedido pedidoAsignadoYActualizado = pedidoUseCase.asignarPedidoYCambiarEstado(emailCliente, idPedido);
        assertNotNull(pedidoAsignadoYActualizado);
        assertEquals(1L, pedidoAsignadoYActualizado.getIdChef());
        assertEquals(EstadoPedido.EN_PREPARACION, pedidoAsignadoYActualizado.getEstado());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteEmpleadoRepository, times(1)).buscarByIdUsuarioEmpleado(anyLong());
        verify(pedidoRepository, times(1)).buscarPorId(anyLong());
        verify(pedidoRepository, times(1)).actualizarPedido(any(Pedido.class));
    }

    @Test
    void asignarPedidoYCambiarEstado_retornaException_cuandoNoExistePedido() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(anyLong()))
                .thenReturn(Optional.of(restauranteEmpleado));
        when(pedidoRepository.buscarPorId(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                pedidoUseCase.asignarPedidoYCambiarEstado(emailCliente, idPedido)
        );
        assertEquals(MensajesEnum.PEDIDO_NO_ENCONTRADO_POR_ID.getMensaje() + idPedido, exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteEmpleadoRepository, times(1)).buscarByIdUsuarioEmpleado(anyLong());
        verify(pedidoRepository, times(1)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).actualizarPedido(any(Pedido.class));
    }

    @Test
    void asignarPedidoYCambiarEstado_retornaException_cuandoNoCoincidenLosRestaurantes() {
        pedido.setIdRestaurante(5L);

        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(usuario));
        when(restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(anyLong()))
                .thenReturn(Optional.of(restauranteEmpleado));
        when(pedidoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(pedido));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                pedidoUseCase.asignarPedidoYCambiarEstado(emailCliente, idPedido)
        );
        assertEquals(MensajesEnum.NO_TIENE_PERMISOS_PARA_ACTUALIZAR_PEDIDO.getMensaje(), exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteEmpleadoRepository, times(1)).buscarByIdUsuarioEmpleado(anyLong());
        verify(pedidoRepository, times(1)).buscarPorId(anyLong());
        verify(pedidoRepository, times(0)).actualizarPedido(any(Pedido.class));
    }
}
