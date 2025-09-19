package co.juan.plazacomidas.usecase.plato;

import co.juan.plazacomidas.model.plato.gateways.PlatoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlatoUseCase {

    private final PlatoRepository platoRepository;
}
