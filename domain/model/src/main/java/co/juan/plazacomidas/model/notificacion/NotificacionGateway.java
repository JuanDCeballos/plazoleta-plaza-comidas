package co.juan.plazacomidas.model.notificacion;

public interface NotificacionGateway {
    void enviarNotificacion(String numeroCelular, String mensaje);
}
