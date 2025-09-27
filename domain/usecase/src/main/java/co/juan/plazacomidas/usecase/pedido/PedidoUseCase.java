package co.juan.plazacomidas.usecase.pedido;

import co.juan.plazacomidas.model.exceptions.ResourceNotFoundException;
import co.juan.plazacomidas.model.notificacion.NotificacionGateway;
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
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class PedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final RestauranteEmpleadoRepository restauranteEmpleadoRepository;
    private final RestauranteRepository restauranteRepository;
    private final PlatoRepository platoRepository;
    private final UsuarioGateway usuarioGateway;
    private final NotificacionGateway notificacionGateway;

    public Pedido crearPedido(String emailCliente, Pedido pedido) {
        Usuario cliente = obtenerUsuarioPorCorreo(emailCliente);
        pedido.setIdCliente(cliente.getIdUsuario());

        if (pedidoRepository.clienteTienePedidoActivo(pedido.getIdCliente())) {
            throw new IllegalArgumentException(MensajesEnum.PEDIDO_EN_PROCESO.getMensaje());
        }

        if (!restauranteRepository.existePorId(pedido.getIdRestaurante())) {
            throw new ResourceNotFoundException(
                    MensajesEnum.RESTAURANTE_NO_ENCONTRADO.getMensaje() + pedido.getIdRestaurante());
        }

        for (PedidoPlato pp : pedido.getPlatos()) {
            Plato plato = platoRepository.buscarPorId(pp.getIdPlato())
                    .orElseThrow(() -> new ResourceNotFoundException(MensajesEnum.PLATO_NO_ENCONTRADO.getMensaje() +
                            pp.getIdPlato()));
            if (!plato.getIdRestaurante().equals(pedido.getIdRestaurante())) {
                throw new IllegalArgumentException(MensajesEnum.PLATO.getMensaje() + plato.getNombre() +
                        MensajesEnum.NO_PERTECENE_A_RESTAURANTE.getMensaje());
            }
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDate.now());

        return pedidoRepository.guardarPedido(pedido);
    }

    public Pagina<Pedido> listarPedidosPorEstado(
            String emailEmpleado, Optional<EstadoPedido> estado, int page, int size) {
        Usuario empleado = obtenerUsuarioPorCorreo(emailEmpleado);

        RestauranteEmpleado restauranteEmpleado = restauranteEmpleadoRepository.buscarByIdUsuarioEmpleado(empleado.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException(MensajesEnum.NO_TIENE_RESTAURANTE_ASIGNADO.getMensaje()));

        return pedidoRepository.listarPedidosPorRestauranteYEstado(
                restauranteEmpleado.getIdRestaurante(), estado, page, size);
    }

    public Pedido asignarPedidoYCambiarEstado(String emailEmpleado, Long idPedido) {
        Usuario empleado = obtenerUsuarioPorCorreo(emailEmpleado);

        RestauranteEmpleado restauranteEmpleado = restauranteEmpleadoRepository.
                buscarByIdUsuarioEmpleado(empleado.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException(MensajesEnum.NO_TIENE_RESTAURANTE_ASIGNADO.getMensaje()));

        Long idRestauranteDelEmpleado = restauranteEmpleado.getIdRestaurante();

        Pedido pedidoAActualizar = buscarPedidoPorId(idPedido);

        if (!pedidoAActualizar.getIdRestaurante().equals(idRestauranteDelEmpleado)) {
            throw new IllegalArgumentException(MensajesEnum.NO_TIENE_PERMISOS_PARA_ACTUALIZAR_PEDIDO.getMensaje());
        }

        if (!pedidoAActualizar.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new IllegalArgumentException(MensajesEnum.PEDIDO_EN_PREPARACION_O_CANCELADO.getMensaje());
        }

        pedidoAActualizar.setIdChef(empleado.getIdUsuario());
        pedidoAActualizar.setEstado(EstadoPedido.EN_PREPARACION);

        return pedidoRepository.actualizarPedido(pedidoAActualizar);
    }

    public Pedido marcarPedidoComoListo(String emailEmpleado, Long idPedido) {
        Usuario empleado = obtenerUsuarioPorCorreo(emailEmpleado);
        Pedido pedidoAActualizar = buscarPedidoPorId(idPedido);

        validarPertenenciaChefYEmpleado(pedidoAActualizar.getIdChef(), empleado.getIdUsuario());

        String pin = String.format("%04d", new java.util.Random().nextInt(10000));

        if (!pedidoAActualizar.getEstado().equals(EstadoPedido.EN_PREPARACION)) {
            throw new IllegalArgumentException(MensajesEnum.PEDIDO_ENTREGADO_O_CANCELADO.getMensaje());
        }

        pedidoAActualizar.setEstado(EstadoPedido.LISTO);
        pedidoAActualizar.setPinEntrega(pin);

        Pedido pedidoGuardado = pedidoRepository.actualizarPedido(pedidoAActualizar);

        Usuario cliente = obtenerUsuarioPorId(pedidoGuardado.getIdCliente());

        String mensaje = MensajesEnum.PEDIDO_LISTO.getMensaje() + pin;
        notificacionGateway.enviarNotificacion(cliente.getCelular(), mensaje);

        return pedidoGuardado;
    }

    public Pedido entregarPedido(String emailEmpleado, Long idPedido, String pinEntregado) {
        Usuario empleado = obtenerUsuarioPorCorreo(emailEmpleado);
        Pedido pedidoAEntregar = buscarPedidoPorId(idPedido);

        validarPertenenciaChefYEmpleado(pedidoAEntregar.getIdChef(), empleado.getIdUsuario());

        if (!pedidoAEntregar.getEstado().equals(EstadoPedido.LISTO)) {
            throw new IllegalArgumentException(MensajesEnum.PEDIDO_ENTREGADO_O_AUN_NO_LISTO.getMensaje());
        }

        if (!pedidoAEntregar.getPinEntrega().equals(pinEntregado)) {
            throw new IllegalArgumentException(MensajesEnum.EL_PIN_DE_ENTREGA_ES_INCORRECTO.getMensaje());
        }

        pedidoAEntregar.setEstado(EstadoPedido.ENTREGADO);

        return pedidoRepository.actualizarPedido(pedidoAEntregar);
    }

    public Pedido cancelarPedido(String emailCliente, Long idPedido) {
        Usuario cliente = obtenerUsuarioPorCorreo(emailCliente);
        Pedido pedidoACancelar = buscarPedidoPorId(idPedido);

        if (!cliente.getIdUsuario().equals(pedidoACancelar.getIdCliente())) {
            throw new IllegalArgumentException(
                    MensajesEnum.NO_PUEDE_CANCELAR_UN_PEDIDO_QUE_NO_LE_PERTENECE.getMensaje());
        }

        if (!pedidoACancelar.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new IllegalArgumentException(MensajesEnum.PEDIDO_EN_PREPARACION_NO_PUEDE_CANCELARSE.getMensaje());
        }

        pedidoACancelar.setEstado(EstadoPedido.CANCELADO);

        return pedidoRepository.actualizarPedido(pedidoACancelar);
    }

    private void validarPertenenciaChefYEmpleado(Long idChef, Long idUsuario) {
        if (!idChef.equals(idUsuario)) {
            throw new IllegalArgumentException(
                    MensajesEnum.NO_PUEDE_MARCAR_COMO_LIST_UN_PEDIDO_AL_CUAL_NO_ESTA_ASIGNADO.getMensaje());
        }
    }

    private Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioGateway.obtenerUsuarioPorCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.USUARIO_NO_ENCONTRADO_POR_EMAIL.getMensaje() + correo));
    }

    private Usuario obtenerUsuarioPorId(Long idUsario) {
        return usuarioGateway.obtenerUsuarioPorId(idUsario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.USUARIO_NO_ENCONTRADO_POR_ID.getMensaje() + idUsario));
    }

    private Pedido buscarPedidoPorId(Long idPedido) {
        return pedidoRepository.buscarPorId(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MensajesEnum.PEDIDO_NO_ENCONTRADO_POR_ID.getMensaje() + idPedido));
    }
}
