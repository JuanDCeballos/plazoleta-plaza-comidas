package co.juan.plazacomidas.jpa.entities;

import co.juan.plazacomidas.model.utils.EstadoPedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(name = "id_chef")
    private Long idChef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante", nullable = false)
    private RestauranteEntity restaurante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoPlatoEntity> pedidoPlatos;
}
