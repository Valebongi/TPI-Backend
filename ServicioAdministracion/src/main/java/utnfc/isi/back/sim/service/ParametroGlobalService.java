package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.sim.domain.ParametroGlobal;
import utnfc.isi.back.sim.repository.ParametroGlobalRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParametroGlobalService {
    private final ParametroGlobalRepository parametroGlobalRepository;

    public List<ParametroGlobal> findAll() { 
        return parametroGlobalRepository.findAll(); 
    }
    
    public Optional<ParametroGlobal> findById(String clave) { 
        return parametroGlobalRepository.findById(clave); 
    }
    
    public ParametroGlobal save(ParametroGlobal parametro) { 
        return parametroGlobalRepository.save(parametro); 
    }
    
    public void deleteById(String clave) { 
        parametroGlobalRepository.deleteById(clave); 
    }
    
    // Métodos de utilidad para obtener valores específicos
    public BigDecimal getValorLitro() {
        return parametroGlobalRepository.findValorByClave("VALOR_LITRO")
            .orElse(BigDecimal.valueOf(150.0)); // valor por defecto
    }
    
    public BigDecimal getCargoGestionTramo() {
        return parametroGlobalRepository.findValorByClave("CARGO_GESTION_TRAMO")
            .orElse(BigDecimal.valueOf(500.0)); // valor por defecto
    }
}