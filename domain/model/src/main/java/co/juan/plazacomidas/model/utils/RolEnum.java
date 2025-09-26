package co.juan.plazacomidas.model.utils;

public enum RolEnum {
    ADMINISTRADOR(1L, "ADMINISTRADOR"),
    PROPIETARIO(2L, "PROPIETARIO"),
    EMPLEADO(3L, "EMPLEADO"),
    CLIENTE(4L, "CLIENTE");

    private Long id;
    private String nombre;

    private RolEnum(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }
}
