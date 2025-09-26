package co.juan.plazacomidas.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedidos_platos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PedidoPlatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plato", nullable = false)
    private PlatoEntity plato;

    @Column(nullable = false)
    private int cantidad;
}
