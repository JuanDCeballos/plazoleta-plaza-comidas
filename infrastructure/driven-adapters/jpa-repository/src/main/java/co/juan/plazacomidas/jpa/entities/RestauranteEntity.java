package co.juan.plazacomidas.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurantes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRestaurante;

    private String nombre;
    private Long nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private Long idUsuario;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlatoEntity> platos;
}
