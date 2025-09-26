package co.juan.plazacomidas.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurante_empleado")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RestauranteEmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante", nullable = false)
    private RestauranteEntity restaurante;

    @Column(name = "id_usuario_empleado", nullable = false)
    private Long idUsuarioEmpleado;
}
