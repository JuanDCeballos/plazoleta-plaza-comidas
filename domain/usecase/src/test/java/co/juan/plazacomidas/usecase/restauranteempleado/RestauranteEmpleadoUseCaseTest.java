package co.juan.plazacomidas.usecase.restauranteempleado;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.restaurante.Restaurante;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import co.juan.plazacomidas.model.restauranteempleado.RestauranteEmpleado;
import co.juan.plazacomidas.model.restauranteempleado.gateways.RestauranteEmpleadoRepository;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteEmpleadoUseCaseTest {

    @InjectMocks
    RestauranteEmpleadoUseCase restauranteEmpleadoUseCase;

    @Mock
    RestauranteEmpleadoRepository restauranteEmpleadoRepository;

    @Mock
    RestauranteRepository restauranteRepository;

    @Mock
    UsuarioGateway usuarioGateway;

    private RestauranteEmpleado restauranteEmpleado;
    private Usuario propietarioLogueado;
    private Usuario empleado;
    private Restaurante restaurante;

    private final String emailPropietarioLogueado = "juan.ceballos@correo.com.co";
    Long idRestaurante = 1L;

    @BeforeEach
    void initMocks() {
        restauranteEmpleado = new RestauranteEmpleado();
        restauranteEmpleado.setId(1L);
        restauranteEmpleado.setIdRestaurante(8L);
        restauranteEmpleado.setIdUsuarioEmpleado(2L);

        propietarioLogueado = new Usuario();
        propietarioLogueado.setIdUsuario(1L);
        propietarioLogueado.setNombre("Juan");
        propietarioLogueado.setApellido("Ceballos");
        propietarioLogueado.setDocumentoDeIdentidad(1374859483L);
        propietarioLogueado.setCelular("3647364738");
        propietarioLogueado.setFechaNacimiento(LocalDate.of(2002, 9, 12));
        propietarioLogueado.setCorreo("juan.ceballos@correo.com.co");
        propietarioLogueado.setIdRol(2L);

        empleado = new Usuario();
        empleado.setIdUsuario(2L);
        empleado.setNombre("David");
        empleado.setApellido("Lopez");
        empleado.setDocumentoDeIdentidad(3647584938L);
        empleado.setCelular("8763425678");
        empleado.setFechaNacimiento(LocalDate.of(2002, 9, 12));
        empleado.setCorreo("david.lopez@correo.com.co");
        empleado.setIdRol(3L);

        restaurante = new Restaurante();
        restaurante.setIdRestaurante(1L);
        restaurante.setNombre("Burger");
        restaurante.setNit(2837495067L);
        restaurante.setDireccion("Cra 88 BB #88-22");
        restaurante.setTelefono("3647584923");
        restaurante.setUrlLogo("https://url.com");
        restaurante.setIdUsuario(1L);
    }

    @Test
    void crearRestauranteEmpleado() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(propietarioLogueado));
        when(restauranteRepository.obtenerById(anyLong())).thenReturn(restaurante);
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(Optional.of(empleado));
        when(restauranteEmpleadoRepository.guardar(any(RestauranteEmpleado.class))).thenReturn(restauranteEmpleado);

        RestauranteEmpleado restauranteEmpleadoGuardado = restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                emailPropietarioLogueado, idRestaurante, restauranteEmpleado);
        assertNotNull(restauranteEmpleadoGuardado);
        assertEquals(1L, restauranteEmpleadoGuardado.getId());
        assertEquals(1L, restauranteEmpleadoGuardado.getIdRestaurante());
        assertEquals(2L, restauranteEmpleadoGuardado.getIdUsuarioEmpleado());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteRepository, times(1)).obtenerById(anyLong());
        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteEmpleadoRepository, times(1)).guardar(any(RestauranteEmpleado.class));
    }

    @Test
    void crearRestauranteEmpleado_retornaException_cuandoNoExisteUsuario() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                    emailPropietarioLogueado, idRestaurante, restauranteEmpleado);
        });
        assertEquals("Usuario no encontrado con el email: " + emailPropietarioLogueado, exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteRepository, times(0)).obtenerById(anyLong());
        verify(usuarioGateway, times(0)).obtenerUsuarioPorId(anyLong());
        verify(restauranteEmpleadoRepository, times(0)).guardar(any(RestauranteEmpleado.class));
    }

    @Test
    void crearRestauranteEmpleado_retornaException_cuandoNoExisteRestaurante() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(propietarioLogueado));
        when(restauranteRepository.obtenerById(anyLong())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                    emailPropietarioLogueado, idRestaurante, restauranteEmpleado);
        });
        assertEquals("Restaurante no encontrado con el id: " +
                idRestaurante, exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteRepository, times(1)).obtenerById(anyLong());
        verify(usuarioGateway, times(0)).obtenerUsuarioPorId(anyLong());
        verify(restauranteEmpleadoRepository, times(0)).guardar(any(RestauranteEmpleado.class));
    }

    @Test
    void crearRestauranteEmpleado_retornaException_usuarioNoPropietario() {
        propietarioLogueado.setIdUsuario(10L);

        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(propietarioLogueado));
        when(restauranteRepository.obtenerById(anyLong())).thenReturn(restaurante);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                    emailPropietarioLogueado, idRestaurante, restauranteEmpleado);
        });
        assertEquals("No tienes permiso para asignar empleados a este restaurante.", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteRepository, times(1)).obtenerById(anyLong());
        verify(usuarioGateway, times(0)).obtenerUsuarioPorId(anyLong());
        verify(restauranteEmpleadoRepository, times(0)).guardar(any(RestauranteEmpleado.class));
    }

    @Test
    void crearRestauranteEmpleado_retornaException_usuairioNoExistePorId() {
        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(propietarioLogueado));
        when(restauranteRepository.obtenerById(anyLong())).thenReturn(restaurante);
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                    emailPropietarioLogueado, idRestaurante, restauranteEmpleado);
        });
        assertEquals("Usuario no encontrado con el id: " +
                restauranteEmpleado.getIdUsuarioEmpleado(), exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteRepository, times(1)).obtenerById(anyLong());
        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteEmpleadoRepository, times(0)).guardar(any(RestauranteEmpleado.class));
    }

    @Test
    void crearRestauranteEmpleado_retornaException_usuairioNoEmpleado() {
        empleado.setIdRol(1L);

        when(usuarioGateway.obtenerUsuarioPorCorreo(anyString())).thenReturn(Optional.of(propietarioLogueado));
        when(restauranteRepository.obtenerById(anyLong())).thenReturn(restaurante);
        when(usuarioGateway.obtenerUsuarioPorId(anyLong())).thenReturn(Optional.of(empleado));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restauranteEmpleadoUseCase.crearRestauranteEmpleado(
                    emailPropietarioLogueado, idRestaurante, restauranteEmpleado);
        });
        assertEquals("El rol debe ser empleado", exception.getMessage());

        verify(usuarioGateway, times(1)).obtenerUsuarioPorCorreo(anyString());
        verify(restauranteRepository, times(1)).obtenerById(anyLong());
        verify(usuarioGateway, times(1)).obtenerUsuarioPorId(anyLong());
        verify(restauranteEmpleadoRepository, times(0)).guardar(any(RestauranteEmpleado.class));
    }
}
