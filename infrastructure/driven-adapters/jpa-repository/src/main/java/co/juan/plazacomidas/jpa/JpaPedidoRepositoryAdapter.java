package co.juan.plazacomidas.jpa;

import co.juan.plazacomidas.jpa.entities.PedidoEntity;
import co.juan.plazacomidas.jpa.entities.PedidoPlatoEntity;
import co.juan.plazacomidas.jpa.entities.PlatoEntity;
import co.juan.plazacomidas.jpa.entities.RestauranteEntity;
import co.juan.plazacomidas.jpa.helper.AdapterOperations;
import co.juan.plazacomidas.jpa.utils.PedidoJpaMapper;
import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.pagina.Pagina;
import co.juan.plazacomidas.model.pedido.Pedido;
import co.juan.plazacomidas.model.pedido.gateways.PedidoRepository;
import co.juan.plazacomidas.model.utils.EstadoPedido;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaPedidoRepositoryAdapter extends AdapterOperations<Pedido, PedidoEntity, Long, JpaPedidoRepository>
        implements PedidoRepository {

    private final JpaRestauranteRepository jpaRestauranteRepository;
    private final JpaPlatoRepository jpaPlatoRepository;
    private final PedidoJpaMapper pedidoJpaMapper;

    public JpaPedidoRepositoryAdapter(JpaPedidoRepository repository, ObjectMapper mapper,
                                      JpaRestauranteRepository jpaRestauranteRepository,
                                      JpaPlatoRepository jpaPlatoRepository,
                                      PedidoJpaMapper pedidoJpaMapper) {
        super(repository, mapper, d -> mapper.map(d, Pedido.class));
        this.jpaRestauranteRepository = jpaRestauranteRepository;
        this.jpaPlatoRepository = jpaPlatoRepository;
        this.pedidoJpaMapper = pedidoJpaMapper;
    }

    @Override
    public Optional<Pedido> buscarPorId(Long idPedido) {
        return repository.findById(idPedido)
                .map(pedidoJpaMapper::toDomain);
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoJpaMapper.toEntity(pedido);

        RestauranteEntity restauranteEntity = jpaRestauranteRepository.findById(pedido.getIdRestaurante())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));
        pedidoEntity.setRestaurante(restauranteEntity);

        List<PedidoPlatoEntity> pedidoPlatos = pedido.getPlatos().stream()
                .map(platoDomain -> {
                    PlatoEntity platoEntity = jpaPlatoRepository.findById(platoDomain.getIdPlato())
                            .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con id: " + platoDomain.getIdPlato()));

                    PedidoPlatoEntity pedidoPlatoEntity = new PedidoPlatoEntity();
                    pedidoPlatoEntity.setPedido(pedidoEntity);
                    pedidoPlatoEntity.setPlato(platoEntity);
                    pedidoPlatoEntity.setCantidad(platoDomain.getCantidad());

                    return pedidoPlatoEntity;
                }).collect(Collectors.toList());

        pedidoEntity.setPedidoPlatos(pedidoPlatos);

        PedidoEntity pedidoGuardado = repository.save(pedidoEntity);

        return pedidoJpaMapper.toDomain(pedidoGuardado);
    }

    @Override
    public Pedido actualizarPedido(Pedido pedido) {
        PedidoEntity pedidoExistente = repository.findById(pedido.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar un pedido que no existe. Id: " + pedido.getId()));

        pedidoExistente.setEstado(pedido.getEstado());
        pedidoExistente.setIdChef(pedido.getIdChef());

        PedidoEntity pedidoActualizado = repository.save(pedidoExistente);

        return pedidoJpaMapper.toDomain(pedidoActualizado);
    }

    @Override
    public boolean clienteTienePedidoActivo(Long idCliente) {
        List<EstadoPedido> estados = Arrays.asList(
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.LISTO
        );

        return repository.clienteTienePedidoActivo(idCliente, estados);
    }

    @Override
    public Pagina<Pedido> listarPedidosPorRestauranteYEstado(
            Long idRestaurante, Optional<EstadoPedido> estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PedidoEntity> pageCustom = repository.findByRestauranteAndEstadoOptional(
                idRestaurante,
                estado.orElse(null),
                pageable
        );

        List<Pedido> contenido = pageCustom.getContent().stream()
                .map(pedidoJpaMapper::toDomain)
                .toList();

        return Pagina.<Pedido>builder()
                .contenido(contenido)
                .totalElementos(pageCustom.getTotalElements())
                .totalPaginas(pageCustom.getTotalPages())
                .numeroPagina(pageCustom.getNumber())
                .tamanoPagina(pageCustom.getSize())
                .build();
    }
}
