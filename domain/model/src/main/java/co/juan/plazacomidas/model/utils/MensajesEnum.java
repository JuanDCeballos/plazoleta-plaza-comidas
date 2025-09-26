package co.juan.plazacomidas.model.utils;

public enum MensajesEnum {
    USUARIO_NO_ENCONTRADO_POR_EMAIL("Usuario no encontrado con el email: "),
    USUARIO_NO_ENCONTRADO_POR_ID("Usuario no encontrado con el id: "),
    PEDIDO_EN_PROCESO("No puede crear un nuevo pedido mientras tenga uno en proceso."),
    RESTAURANTE_NO_ENCONTRADO("Restaurante no encontrado con el id: "),
    PLATO_NO_ENCONTRADO("Plato no encontrado con el id: "),
    PLATO("El plato "),
    NO_PERTECENE_A_RESTAURANTE(" no pertenece a este restaurante."),
    NO_TIENE_RESTAURANTE_ASIGNADO("No estás asignado a ningún restaurante."),
    CATEGORIA_NO_ENCONTRADA_POR_ID("Categoria no encontrada con el id: "),
    ROL_PROPIETARIO("El rol debe ser propietario"),
    NO_TIENE_PERMISOS_PARA_ASIGNAR_EMPLEADO("No tienes permiso para asignar empleados a este restaurante."),
    ROL_EMPLEADO("El rol debe ser empleado");

    private String mensaje;

    private MensajesEnum(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return this.mensaje;
    }
}
