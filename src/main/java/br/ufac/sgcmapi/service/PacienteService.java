package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.ufac.sgcmapi.model.Paciente;
import br.ufac.sgcmapi.repository.PacienteRepository;

@Service
public class PacienteService implements ICrudService<Paciente> {

    private final PacienteRepository repo;

    public PacienteService(PacienteRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Paciente> consultar(String termoBusca) {
        if (termoBusca != null) {
            termoBusca = StringUtils.trimAllWhitespace(termoBusca);
        }
        return repo.consultar(termoBusca);
    }

    @Override
    public Paciente consultar(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Paciente salvar(Paciente objeto) {
        return repo.save(objeto);
    }

    @Override
    public void remover(Long id) {
        var registro = this.consultar(id);
        if (registro != null) {
            repo.deleteById(id);
        }
    }
    
}
