package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.sim.domain.Deposito;
import utnfc.isi.back.sim.repository.DepositoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepositoService {
    private final DepositoRepository depositoRepository;

    public List<Deposito> findAll() { return depositoRepository.findAll(); }
    public Optional<Deposito> findById(Integer id) { return depositoRepository.findById(id); }
    public Deposito save(Deposito d) { return depositoRepository.save(d); }
    public void deleteById(Integer id) { depositoRepository.deleteById(id); }
}
