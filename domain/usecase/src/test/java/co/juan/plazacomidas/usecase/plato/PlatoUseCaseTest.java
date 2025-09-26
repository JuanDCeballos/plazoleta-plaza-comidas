package co.juan.plazacomidas.usecase.plato;

import co.juan.plazacomidas.model.categoria.gateways.CategoriaRepository;
import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.plato.Plato;
import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import co.juan.plazacomidas.model.restaurante.gateways.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @InjectMocks
    PlatoUseCase platoUseCase;

    @Mock
    PlatoRepository platoRepository;

    @Mock
    RestauranteRepository restauranteRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    private Plato plato;
    private Plato platoConNuevosDatos;
    private Long idRestaurante = 1L;
    private final Long idPlato = 1L;
    private final boolean estado = false;
    private Pagina<Plato> platosPaginados;
    private final Long idCategoria = 1L;

    private final int page = 0;
    private final int size = 5;

    @BeforeEach
    void initMocks() {
        plato = new Plato();
        plato.setIdPlato(1L);
        plato.setNombre("Hamburguesa");
        plato.setPrecio(new BigDecimal("35000"));
        plato.setDescripcion("Pan, carne, tomate");
        plato.setUrlImagen("https://url.com");
        plato.setCategoria(1L);
        plato.setActivo(true);
        plato.setIdRestaurante(1L);

        platoConNuevosDatos = new Plato();
        platoConNuevosDatos.setPrecio(new BigDecimal("38000"));
        platoConNuevosDatos.setDescripcion("Pan, carne, tomate, lechuga, cebolla");

        platosPaginados = new Pagina<>();
        platosPaginados.setContenido(List.of(plato));
        platosPaginados.setTotalElementos(3L);
        platosPaginados.setTotalPaginas(2);
        platosPaginados.setNumeroPagina(0);
        platosPaginados.setTamanoPagina(5);
    }

    @Test
    void crearPlato() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(categoriaRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.guardarPlato(any(Plato.class))).thenReturn(plato);

        Plato platoCreado = platoUseCase.crearPlato(idRestaurante, plato);
        assertNotNull(platoCreado);
        assertEquals(1L, platoCreado.getIdPlato());
        assertEquals("Hamburguesa", platoCreado.getNombre());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(categoriaRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).guardarPlato(any(Plato.class));
    }

    @Test
    void crearPlato_retornaException_cuandoNoExisteRestaurante() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                platoUseCase.crearPlato(idRestaurante, plato));
        assertEquals("Restaurante no encontrado con el id: " + idRestaurante, exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(categoriaRepository, times(0)).existePorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void crearPlato_retornaException_cuandoNoExisteCategoria() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(categoriaRepository.existePorId(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                platoUseCase.crearPlato(idRestaurante, plato));
        assertEquals("Categoria no encontrada con el id: " + plato.getCategoria(), exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(categoriaRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));
        when(platoRepository.guardarPlato(any(Plato.class))).thenReturn(plato);

        Plato platoActualizado = platoUseCase.actualizarPlato(idRestaurante, idPlato, platoConNuevosDatos);
        assertNotNull(platoActualizado);
        assertEquals(new BigDecimal("38000"), platoActualizado.getPrecio());
        assertEquals("Pan, carne, tomate, lechuga, cebolla", platoActualizado.getDescripcion());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(platoRepository, times(1)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_retornaException_cuandoNoExistePlato() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                platoUseCase.actualizarPlato(idRestaurante, idPlato, platoConNuevosDatos)
        );
        assertEquals("Plato no encontrado con el id: " + idPlato, exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_retornaException_cuandoNoExisteRestaurante() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                platoUseCase.actualizarPlato(idRestaurante, idPlato, platoConNuevosDatos));
        assertEquals("Restaurante no encontrado con el id: " + idRestaurante, exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(0)).buscarPorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_retornaException_cuandoNoCoincidenLosRestaurantes() {
        idRestaurante = 5L;

        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                platoUseCase.actualizarPlato(idRestaurante, idPlato, platoConNuevosDatos));
        assertEquals("El plato " + plato.getNombre() + " no pertenece a este restaurante.", exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_guardaPlato_cuandoNoHayValoresPorActualizar() {
        platoConNuevosDatos.setPrecio(null);
        platoConNuevosDatos.setDescripcion(null);

        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));
        when(platoRepository.guardarPlato(any(Plato.class))).thenReturn(plato);

        Plato platoActualizado = platoUseCase.actualizarPlato(idRestaurante, idPlato, platoConNuevosDatos);
        assertNotNull(platoActualizado);
        assertEquals(new BigDecimal("35000"), platoActualizado.getPrecio());
        assertEquals("Pan, carne, tomate", platoActualizado.getDescripcion());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(platoRepository, times(1)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarEstadoPlato() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));
        when(platoRepository.guardarPlato(any(Plato.class))).thenReturn(plato);

        Plato platoActualizado = platoUseCase.actualizarEstadoPlato(idRestaurante, idPlato, estado);
        assertNotNull(platoActualizado);
        assertEquals(new BigDecimal("35000"), platoActualizado.getPrecio());
        assertEquals("Pan, carne, tomate", platoActualizado.getDescripcion());
        assertFalse(platoActualizado.isActivo());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(platoRepository, times(1)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarEstadoPlato_retornaException_cuandoNoExisteRestaurante() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                platoUseCase.actualizarEstadoPlato(idRestaurante, idPlato, estado));
        assertEquals("Restaurante no encontrado con el id: " + idRestaurante, exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(0)).buscarPorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarEstadoPlato_retornaException_cuandoNoCoincidenLosRestaurantes() {
        idRestaurante = 5L;

        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.buscarPorId(anyLong())).thenReturn(Optional.of(plato));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                platoUseCase.actualizarEstadoPlato(idRestaurante, idPlato, estado));
        assertEquals("El plato " + plato.getNombre() + " no pertenece a este restaurante.", exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).buscarPorId(anyLong());
        verify(platoRepository, times(0)).guardarPlato(any(Plato.class));
    }

    @Test
    void listarPlatosDeRestaurantes() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(true);
        when(platoRepository.listarPlatosPorRestaurante(anyLong(), any(Optional.class),
                anyInt(), anyInt())).thenReturn(platosPaginados);

        Pagina<Plato> datosPaginados = platoUseCase.listarPlatosDeRestaurante(idRestaurante,
                Optional.of(idCategoria), page, size);
        assertNotNull(datosPaginados);
        assertEquals("Hamburguesa", datosPaginados.getContenido().getFirst().getNombre());
        assertEquals(3L, datosPaginados.getTotalElementos());
        assertEquals(2, datosPaginados.getTotalPaginas());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(1)).listarPlatosPorRestaurante(anyLong(), any(Optional.class),
                anyInt(), anyInt());
    }

    @Test
    void listarPlatosDeRestaurantes_retornaException_cuandoNoExisteRestaurante() {
        when(restauranteRepository.existePorId(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> platoUseCase.listarPlatosDeRestaurante(idRestaurante,
                Optional.of(idCategoria), page, size));
        assertEquals("Restaurante no encontrado con el id: " + idRestaurante, exception.getMessage());

        verify(restauranteRepository, times(1)).existePorId(anyLong());
        verify(platoRepository, times(0)).listarPlatosPorRestaurante(anyLong(), any(Optional.class),
                anyInt(), anyInt());
    }
}
