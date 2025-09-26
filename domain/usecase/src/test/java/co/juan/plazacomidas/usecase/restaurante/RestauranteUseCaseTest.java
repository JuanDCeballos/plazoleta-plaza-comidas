package co.juan.plazacomidas.usecase.restaurante;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @InjectMocks
    RestauranteUseCase restauranteUseCase;

    @Mock
    RestauranteRepository restauranteRepository;

    @Mock
    UsuarioGateway usuarioGateway;

    private Restaurante restaurante;
    private Usuario usuario;
    private Pagina<Restaurante> restaurantePagina;

    @BeforeEach
    void initMocks() {
        restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        restaurante.setNombre("Burger");
        restaurante.setNit(2837495067L);
        restaurante.setDireccion("Cra 88 BB #88-22");
        restaurante.setTelefono("3647584923");
        restaurante.setUrlLogo("https://url.com");
        restaurante.setIdUsuario(1L);

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Ceballos");
        usuario.setDocumentoDeIdentidad(1374859483L);
        usuario.setCelular("3647364738");
        usuario.setFechaNacimiento(LocalDate.of(2002, 9, 12));
        usuario.setCorreo("juan.ceballos@correo.com.co");
        usuario.setIdRol(2L);

        restaurantePagina = new Pagina<>();
        restaurantePagina.setContenido(List.of(restaurante));
        restaurantePagina.setTotalElementos(3L);
        restaurantePagina.setTotalPaginas(2);
        restaurantePagina.setNumeroPagina(0);
        restaurantePagina.setTamanoPagina(5);
    }

    @Test
    void crearRestaurante() {
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(Optional.of(usuario));
        when(restauranteRepository.crearRestaurante(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante restauranteCreado = restauranteUseCase.crearRestaurante(restaurante);
        assertNotNull(restauranteCreado);
        assertEquals(1L, restauranteCreado.getIdRestaurante());
        assertEquals("Burger", restauranteCreado.getNombre());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteRepository, times(1)).crearRestaurante(any(Restaurante.class));
    }

    @Test
    void crearRestaurante_retornaException_usuarioNull() {
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteUseCase.crearRestaurante(restaurante);
        });
        assertEquals("Usuario no encontrado con el id: " + restaurante.getIdUsuario(), exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteRepository, times(0)).crearRestaurante(any(Restaurante.class));
    }

    @Test
    void crearRestaurante_retornaException_usuarioDiferenteAPropietario() {
        usuario.setIdRol(1L);

        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(Optional.of(usuario));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restauranteUseCase.crearRestaurante(restaurante);
        });
        assertEquals("El rol debe ser propietario", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteRepository, times(0)).crearRestaurante(any(Restaurante.class));
    }

    @Test
    void listarRestaurantes() {
        int page = 0;
        int size = 5;

        when(restauranteRepository.listarRestaurantesPaginados(anyInt(), anyInt())).thenReturn(restaurantePagina);

        Pagina<Restaurante> restaurantePaginados = restauranteUseCase.listarRestaurantes(page, size);
        assertNotNull(restaurantePaginados);
        assertEquals("Burger", restaurantePaginados.getContenido().getFirst().getNombre());
        assertEquals(3L, restaurantePaginados.getTotalElementos());
        assertEquals(2, restaurantePaginados.getTotalPaginas());

        verify(restauranteRepository, times(1)).listarRestaurantesPaginados(anyInt(), anyInt());
    }
}
