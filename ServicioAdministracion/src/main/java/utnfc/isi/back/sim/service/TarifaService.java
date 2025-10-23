package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.sim.domain.Tarifa;
import utnfc.isi.back.sim.repository.TarifaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarifaService {
    private final TarifaRepository tarifaRepository;

    public List<Tarifa> findAll() { return tarifaRepository.findAll(); }
    public Optional<Tarifa> findById(Integer id) { return tarifaRepository.findById(id); }
    public Tarifa save(Tarifa t) { return tarifaRepository.save(t); }
    public void deleteById(Integer id) { tarifaRepository.deleteById(id); }
}
