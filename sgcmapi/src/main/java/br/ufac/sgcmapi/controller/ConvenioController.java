package br.ufac.sgcmapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufac.sgcmapi.model.Convenio;
import br.ufac.sgcmapi.service.ConvenioService;

@RestController
@RequestMapping("/convenio")
public class ConvenioController implements ICrudController<Convenio> {

    private final ConvenioService servico;

    public ConvenioController(ConvenioService servico) {
        this.servico = servico;
    }

    @Override
    public ResponseEntity<List<Convenio>> consultar(String termoBusca) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultar'");
    }

    @Override
    public ResponseEntity<Convenio> consultar(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultar'");
    }

    @Override
    public ResponseEntity<Long> inserir(Convenio objeto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inserir'");
    }

    @Override
    public ResponseEntity<Void> atualizar(Convenio objeto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

    @Override
    public ResponseEntity<Void> remover(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remover'");
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Convenio>> consultarAtivos() {
        var registros = servico.consultarAtivos();
        return ResponseEntity.ok(registros);
    }
    
}
