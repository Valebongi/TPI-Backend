package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.sim.domain.Camion;
import utnfc.isi.back.sim.repository.CamionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CamionService {
    private final CamionRepository camionRepository;

    public List<Camion> findAll() { return camionRepository.findAll(); }
    public Optional<Camion> findById(Integer id) { return camionRepository.findById(id); }
    public Camion save(Camion c) { return camionRepository.save(c); }
    public void deleteById(Integer id) { camionRepository.deleteById(id); }
    public Camion findByDominio(String dominio) { return camionRepository.findByDominio(dominio); }
}
