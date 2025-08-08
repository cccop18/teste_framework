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
public class ConvenioTest {

	private final MockMvc mockMvc;

	@Autowired
	ConvenioTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

    @Test
    @Order(1)
    void testConvenioConsultarTodos() throws Exception {
        mockMvc.perform(get("/convenio/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].nome", is("Unimed")))
            .andExpect(jsonPath("$[1].nome", is("Amil")))
            .andExpect(jsonPath("$[2].nome", is("Bradesco")));
    }

    @Test
    @Order(2)
    void testConvenioConsultarPorId() throws Exception {
        mockMvc.perform(get("/convenio/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is("Unimed")));
    }

    @Test
    @Order(3)
    void testConvenioConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/convenio/consultar")
            .param("termoBusca", "Unimed"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nome", is("Unimed")));
    }

    @Test
    @Order(4)
    void testConvenioInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/convenioInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/convenio/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(4)));

    }

    @Test
    @Order(5)
    void testConvenioAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/convenioAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(put("/convenio/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/convenio/consultar/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ativo", is(false)));

    }

    @Test
    @Order(6)
    void testConvenioRemover() throws Exception {

        mockMvc.perform(delete("/convenio/remover/4"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/convenio/consultar"))
            .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    @Order(7)
    void testConvenioConsultarAtivos() throws Exception {
        mockMvc.perform(get("/convenio/ativos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nome", is("Unimed")))
            .andExpect(jsonPath("$[1].nome", is("Amil")));
    }

    @Test
    @Order(8)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/convenio/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/convenio/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
