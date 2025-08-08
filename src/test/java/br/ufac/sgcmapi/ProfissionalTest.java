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
public class ProfissionalTest {

	private final MockMvc mockMvc;

	@Autowired
	ProfissionalTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

    @Test
    @Order(1)
    void testProfissionalConsultarTodos() throws Exception {
        mockMvc.perform(get("/profissional/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].nome", is("Maria Adelia Serravalle Bezerra")))
            .andExpect(jsonPath("$[1].nome", is("Elielson Silveira Andrade")))
            .andExpect(jsonPath("$[2].nome", is("Davi Jesus Mendes")))
            .andExpect(jsonPath("$[3].nome", is("Carla da Paixão Valle")))
            .andExpect(jsonPath("$[4].nome", is("Neuza Biango Nobrega")));
    }

    @Test
    @Order(2)
    void testProfissionalConsultarPorId() throws Exception {
        mockMvc.perform(get("/profissional/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Maria Adelia Serravalle Bezerra")));
    }

    @Test
    @Order(3)
    void testProfissionalConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/profissional/consultar")
            .param("termoBusca", "alle"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nome", is("Maria Adelia Serravalle Bezerra")));
    }

    @Test
    @Order(4)
    void testProfissionalInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/profissionalInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/profissional/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(6)));

    }

    @Test
    @Order(5)
    void testProfissionalAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/profissionalAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(put("/profissional/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/profissional/consultar/6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.especialidade.id", is(2)));

    }

    @Test
    @Order(6)
    void testProfissionalRemover() throws Exception {

        mockMvc.perform(delete("/profissional/remover/6"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/profissional/consultar"))
            .andExpect(jsonPath("$", hasSize(5)));

    }

    @Test
    @Order(7)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/profissional/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/profissional/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
