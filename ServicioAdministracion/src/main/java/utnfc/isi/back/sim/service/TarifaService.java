package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfc.isi.back.sim.domain.Tarifa;
import utnfc.isi.back.sim.repository.TarifaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {
    private final TarifaRepository tarifaRepository;

    @Autowired
    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<Tarifa> findAll() { return tarifaRepository.findAll(); }
    public Optional<Tarifa> findById(Long id) { return tarifaRepository.findById(id); }
    public Tarifa save(Tarifa t) { return tarifaRepository.save(t); }
    public void deleteById(Long id) { tarifaRepository.deleteById(id); }
}
