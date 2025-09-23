package co.juan.plazacomidas.api.dto.plato;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActualizarEstadoPlatoDto {

    @NotNull(message = "El estado 'activo' es obligatorio")
    private Boolean activo;
}
