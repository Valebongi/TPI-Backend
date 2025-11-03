package utnfc.isi.back.sim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.model.DistanciaDTO;
import utnfc.isi.back.sim.service.GeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/distancia")
@RequiredArgsConstructor
@Tag(name = "Geolocalizacion", description = "API para cálculo de distancias usando Google Maps")
public class GeoController {

    private final GeoService geoService;

    @GetMapping
    @Operation(summary = "Calcular distancia entre dos puntos", 
               description = "Calcula la distancia en kilómetros y duración entre dos coordenadas geográficas usando Google Maps Distance Matrix API")
    public DistanciaDTO obtenerDistancia(
            @Parameter(description = "Coordenadas de origen en formato latitud,longitud", 
                      example = "-31.4167,-64.1833")
            @RequestParam String origen,
            @Parameter(description = "Coordenadas de destino en formato latitud,longitud", 
                      example = "-32.8908,-68.8272") 
            @RequestParam String destino) throws Exception {
        return geoService.calcularDistancia(origen, destino);
    }
}