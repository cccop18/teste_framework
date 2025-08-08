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
public class EspecialidadeTest {

	private final MockMvc mockMvc;

	@Autowired
	EspecialidadeTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

    @Test
    @Order(1)
    void testEspecialidadeConsultarTodos() throws Exception {
        mockMvc.perform(get("/config/especialidade/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(7)))
            .andExpect(jsonPath("$[0].nome", is("Cardiologia")))
            .andExpect(jsonPath("$[1].nome", is("Dermatologia")))
            .andExpect(jsonPath("$[2].nome", is("Geriatria")))
            .andExpect(jsonPath("$[3].nome", is("Infectologia")))
            .andExpect(jsonPath("$[4].nome", is("Pediatria")))
            .andExpect(jsonPath("$[5].nome", is("Psiquiatria")))
            .andExpect(jsonPath("$[6].nome", is("Urologia")));
    }

    @Test
    @Order(2)
    void testEspecialidadeConsultarPorId() throws Exception {
        mockMvc.perform(get("/config/especialidade/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Cardiologia")));
    }

    @Test
    @Order(3)
    void testEspecialidadeConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/config/especialidade/consultar")
            .param("termoBusca", "gia"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].nome", is("Cardiologia")));
    }

    @Test
    @Order(4)
    void testEspecialidadeInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/especialidadeInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/config/especialidade/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(8)));

    }

    @Test
    @Order(5)
    void testEspecialidadeAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/especialidadeAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(put("/config/especialidade/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/config/especialidade/consultar/8"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Ortopedia")));

    }

    @Test
    @Order(6)
    void testEspecialidadeRemover() throws Exception {

        mockMvc.perform(delete("/config/especialidade/remover/8"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/config/especialidade/consultar"))
            .andExpect(jsonPath("$", hasSize(7)));

    }

    @Test
    @Order(7)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/config/especialidade/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/config/especialidade/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
