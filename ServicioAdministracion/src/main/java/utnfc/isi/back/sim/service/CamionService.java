package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfc.isi.back.sim.domain.Camion;
import utnfc.isi.back.sim.repository.CamionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CamionService {
    private final CamionRepository camionRepository;

    @Autowired
    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    public List<Camion> findAll() { return camionRepository.findAll(); }
    public Optional<Camion> findById(Long id) { return camionRepository.findById(id); }
    public Camion save(Camion c) { return camionRepository.save(c); }
    public void deleteById(Long id) { camionRepository.deleteById(id); }
    public Camion findByDominio(String dominio) { return camionRepository.findByDominio(dominio); }
}
