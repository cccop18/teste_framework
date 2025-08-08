package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufac.sgcmapi.model.Profissional;
import br.ufac.sgcmapi.repository.ProfissionalRepository;

@Service
public class ProfissionalService implements ICrudService<Profissional> {

    private final ProfissionalRepository repo;

    public ProfissionalService(ProfissionalRepository repo) {
        this.repo = repo;
    }
    
    public List<Profissional> consultar(@RequestParam(required = false)String termoBusca) {
        return repo.consultar(StringUtils.trimAllWhitespace(termoBusca));
    }

    @Override
    public Profissional consultar(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Profissional salvar(Profissional objeto) {
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
