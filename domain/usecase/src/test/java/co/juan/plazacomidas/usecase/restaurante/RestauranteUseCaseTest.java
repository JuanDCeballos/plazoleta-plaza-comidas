package co.juan.plazacomidas.usecase.restaurante;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
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
    }

    @Test
    void crearRestaurante() {
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(usuario);
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
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteUseCase.crearRestaurante(restaurante);
        });
        assertEquals("El usuario con el ID proporcionado no existe.", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteRepository, times(0)).crearRestaurante(any(Restaurante.class));
    }

    @Test
    void crearRestaurante_retornaException_usuarioDiferenteAPropietario() {
        usuario.setIdRol(1L);

        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(usuario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restauranteUseCase.crearRestaurante(restaurante);
        });
        assertEquals("El rol debe ser propietario", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteRepository, times(0)).crearRestaurante(any(Restaurante.class));
    }
}
