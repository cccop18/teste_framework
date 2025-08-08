package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufac.sgcmapi.model.Especialidade;
import br.ufac.sgcmapi.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService implements ICrudService<Especialidade> {

    private final EspecialidadeRepository repo;

    public EspecialidadeService(EspecialidadeRepository repo) {
        this.repo = repo;
    }
    
    public List<Especialidade> consultar(@RequestParam(required = false)String termoBusca) {
        return repo.consultar(StringUtils.trimAllWhitespace(termoBusca));
    }

    @Override
    public Especialidade consultar(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Especialidade salvar(Especialidade objeto) {
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
