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
public class UnidadeTest {

	private final MockMvc mockMvc;

	@Autowired
	UnidadeTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

    @Test
    @Order(1)
    void testUnidadeConsultarTodos() throws Exception {
        mockMvc.perform(get("/config/unidade/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].nome", is("Bela Vista")))
            .andExpect(jsonPath("$[1].nome", is("Bosque")))
            .andExpect(jsonPath("$[2].nome", is("Cruzeiro do Sul")));
    }

    @Test
    @Order(2)
    void testUnidadeConsultarPorId() throws Exception {
        mockMvc.perform(get("/config/unidade/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Bela Vista")));
    }

    @Test
    @Order(3)
    void testUnidadeConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/config/unidade/consultar").param("termoBusca", "Rua"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].nome", is("Bela Vista")));
    }

    @Test
    @Order(4)
    void testUnidadeInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/unidadeInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/config/unidade/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(4)));

    }

    @Test
    @Order(5)
    void testUnidadeAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/unidadeAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(put("/config/unidade/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/config/unidade/consultar/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.endereco", is("Via Chico Mendes, 1780 - Triângulo")));

    }

    @Test
    @Order(6)
    void testUnidadeRemover() throws Exception {

        mockMvc.perform(delete("/config/unidade/remover/4"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/config/unidade/consultar"))
            .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    @Order(7)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/config/unidade/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/config/unidade/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
