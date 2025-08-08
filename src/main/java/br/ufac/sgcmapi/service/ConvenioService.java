package br.ufac.sgcmapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.ufac.sgcmapi.model.Convenio;
import br.ufac.sgcmapi.repository.ConvenioRepository;

@Service
public class ConvenioService implements ICrudService<Convenio> {

    private final ConvenioRepository repo;

    public ConvenioService(ConvenioRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Convenio> consultar(String termoBusca) {
        return repo.consultar(StringUtils.trimAllWhitespace(termoBusca));
    }

    @Override
    public Convenio consultar(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Convenio salvar(Convenio objeto) {
        return repo.save(objeto);
    }

    @Override
    public void remover(Long id) {
        var registro = this.consultar(id);
        if (registro != null) {
            repo.deleteById(id);
        }
    }

    public List<Convenio> consultarAtivos() {
        return repo.findByAtivo(true);
    }
    
}
