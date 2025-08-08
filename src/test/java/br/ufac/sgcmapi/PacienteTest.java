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
public class PacienteTest {

	private final MockMvc mockMvc;

	@Autowired
	PacienteTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

    @Test
    @Order(1)
    void testPacienteConsultarTodos() throws Exception {
        mockMvc.perform(get("/paciente/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].nome", is("Giulia Farias Bencatel")))
            .andExpect(jsonPath("$[1].nome", is("Wallace Macedo Inácio Soriano")))
            .andExpect(jsonPath("$[2].nome", is("Helen Dutra Vilar")))
            .andExpect(jsonPath("$[3].nome", is("Jean Schuenck Amorin")))
            .andExpect(jsonPath("$[4].nome", is("Lucilene Santos Lucas")));
    }

    @Test
    @Order(2)
    void testPacienteConsultarPorId() throws Exception {
        mockMvc.perform(get("/paciente/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Giulia Farias Bencatel")));
    }

    @Test
    @Order(3)
    void testPacienteConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/paciente/consultar")
            .param("termoBusca", "Giulia"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nome", is("Giulia Farias Bencatel")));
    }

    @Test
    @Order(4)
    void testPacienteInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/pacienteInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/paciente/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(6)));

    }

    @Test
    @Order(5)
    void testPacienteAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/pacienteAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(put("/paciente/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/paciente/consultar/6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.grupoSanguineo", is("AB_POSITIVO")));

    }

    @Test
    @Order(6)
    void testPacienteRemover() throws Exception {

        mockMvc.perform(delete("/paciente/remover/6"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/paciente/consultar"))
            .andExpect(jsonPath("$", hasSize(5)));

    }

    @Test
    @Order(7)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/paciente/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/paciente/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
