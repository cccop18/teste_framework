package br.ufac.sgcmapi;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufac.sgcmapi.model.Atendimento;
import br.ufac.sgcmapi.model.Convenio;
import br.ufac.sgcmapi.model.Paciente;
import br.ufac.sgcmapi.model.Profissional;
import br.ufac.sgcmapi.repository.AtendimentoRepository;


@SpringBootApplication
@RestController
public class SgcmapiApplication {

	@Autowired
	private ExemploService exemploService;

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	// public SgcmapiApplication(ExemploService exemploService) {
	// 	this.exemploService = exemploService;
	// }

	@RequestMapping(value = "/")
	public String exemplo() {
		// return "SGCM";
		return exemploService.exibirMensagem();
	}

	@RequestMapping(value = "/teste")
	public String teste() {
		var atendimentos = atendimentoRepository.findAll();
		var resultado = new StringBuilder();
		for (var item : atendimentos) {
			resultado.append(item.getData() + "\n");
			resultado.append(item.getHora() + "\n");
			resultado.append(item.getPaciente().getNome() + "\n");
			resultado.append(item.getProfissional().getNome() + "\n");
			if (item.getConvenio() != null) {
				resultado.append(item.getConvenio().getNome() + "\n");
			}
			resultado.append(item.getStatus() + "\n");
			resultado.append("\n");
		}
		return "<pre>" + resultado.toString() + "</pre>";
	}

	@RequestMapping(value = "/salvar")
	public String salvar() {
		var atendimento = new Atendimento();
		var profissional = new Profissional();
		var paciente = new Paciente();
		var convenio = new Convenio();

		profissional.setId(1L);
		paciente.setId(1L);
		convenio.setId(1L);

		atendimento.setData(LocalDate.of(2025, 8, 1));
		atendimento.setHora(LocalTime.of(14, 0));
		atendimento.setProfissional(profissional);
		atendimento.setPaciente(paciente);
		atendimento.setConvenio(convenio);

		atendimentoRepository.save(atendimento);

		return "Registro inserido com sucesso";
	}

	public static void main(String[] args) {
		SpringApplication.run(SgcmapiApplication.class, args);
	}

	@Service
	public static class ExemploService {
		public String exibirMensagem() {
			return "SGCM funcionando!";
		}
	}

}
