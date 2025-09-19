package co.juan.plazacomidas.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "platos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlato;

    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private String urlImagen;
    private Long categoria; //TODO: REVISAR COMO SE VAN A MANEJAR ESTAS CATEGORIAS
}
