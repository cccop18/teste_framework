package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.ufac.sgcmapi.model.Unidade;
import br.ufac.sgcmapi.repository.UnidadeRepository;

@Service
public class UnidadeService implements ICrudService<Unidade> {
    
    private final UnidadeRepository repo;

    public UnidadeService(UnidadeRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Unidade> consultar(String termoBusca) {
        if (termoBusca != null) {
            termoBusca = StringUtils.trimAllWhitespace(termoBusca);
        }
        return repo.consultar(termoBusca);
    }

    @Override
    public Unidade consultar(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Unidade salvar(Unidade objeto) {
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
