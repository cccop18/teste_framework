package br.ufac.sgcmapi;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AtendimentoTest {

	private final MockMvc mockMvc;

	@Autowired
	AtendimentoTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
    
    @Test
	@Order(1)
	void testAtendimentoConsultarTodos() throws Exception {
        mockMvc.perform(get("/atendimento/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].hora", is("14:00:00")))
            .andExpect(jsonPath("$[1].hora", is("14:00:00")))
            .andExpect(jsonPath("$[2].hora", is("14:30:00")))
            .andExpect(jsonPath("$[3].hora", is("15:00:00")))
            .andExpect(jsonPath("$[4].hora", is("15:00:00")));
    }

	@Test
	@Order(2)
	void testAtendimentoConsultarPorId() throws Exception {
        mockMvc.perform(get("/atendimento/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hora", is("14:00:00")));
    }

	@Test
	@Order(3)
	void testAtendimentoConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/atendimento/consultar")
            .param("termoBusca", "Maria"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].hora", is("14:00:00")))
            .andExpect(jsonPath("$[1].hora", is("15:00:00")));
    }

	@Test
	@Order(4)
	void testAtendimentoInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/atendimentoInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/atendimento/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(6)));
        
    }

	@Test
	@Order(5)
	void testAtendimentoAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/atendimentoAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());
    
        mockMvc.perform(put("/atendimento/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/atendimento/consultar/6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hora", is("17:00:00")));

    }

	@Test
	@Order(6)
	void testAtendimentoRemover() throws Exception {
        mockMvc.perform(delete("/atendimento/remover/1"))
            .andExpect(status().isOk());
    }

	@Test
	@Order(7)
	void testAtendimentoAtualizarStatus() throws Exception {
        mockMvc.perform(put("/atendimento/status/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is("CONFIRMADO")));
    }

    @Test
    @Order(8)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/atendimento/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/atendimento/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
