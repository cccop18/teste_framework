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
public class UsuarioTest {

	private final MockMvc mockMvc;

	@Autowired
	UsuarioTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

    @Test
    @Order(1)
    void testUsuarioConsultarTodos() throws Exception {
        mockMvc.perform(get("/config/usuario/consultar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].nomeCompleto", is("Administrador")))
            .andExpect(jsonPath("$[1].nomeCompleto", is("Daniel")))
            .andExpect(jsonPath("$[2].nomeCompleto", is("Paulo")));
    }

    @Test
    @Order(2)
    void testUsuarioConsultarPorId() throws Exception {
        mockMvc.perform(get("/config/usuario/consultar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nomeCompleto", is("Administrador")));
    }

    @Test
    @Order(3)
    void testUsuarioConsultarPorTermoBusca() throws Exception {
        mockMvc.perform(get("/config/usuario/consultar").param("termoBusca", "admin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nomeCompleto", is("Administrador")));
    }

    @Test
    @Order(4)
    void testUsuarioInserir() throws Exception {

        File jsonFile = new ClassPathResource("json/usuarioInserir.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(post("/config/usuario/inserir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", is(4)));

    }

    @Test
    @Order(5)
    void testUsuarioAtualizar() throws Exception {

        File jsonFile = new ClassPathResource("json/usuarioAtualizar.json").getFile();
        String jsonContent = Files.readString(jsonFile.toPath());

        mockMvc.perform(put("/config/usuario/atualizar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isOk());
    
        mockMvc.perform(get("/config/usuario/consultar/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nomeCompleto", is("Laura Costa Sarkis")));

    }

    @Test
    @Order(6)
    void testUsuarioRemover() throws Exception {

        mockMvc.perform(delete("/config/usuario/remover/4"))
            .andExpect(status().isOk());

        mockMvc.perform(get("/config/usuario/consultar"))
            .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    @Order(7)
    void testRegistroNaoEncontrado() throws Exception {
        mockMvc.perform(get("/config/usuario/consultar/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/config/usuario/consultar/123"))
            .andExpect(status().isNotFound());
    }

}
