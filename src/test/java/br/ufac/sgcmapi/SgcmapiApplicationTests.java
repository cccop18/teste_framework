package br.ufac.sgcmapi;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class SgcmapiApplicationTests {

	private final MockMvc mockMvc;

	@Autowired
	SgcmapiApplicationTests(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testHorariosOcupadosProfissional() throws Exception {
		String url = "/atendimento/horarios-ocupados-profissional";
		mockMvc.perform(get(url)
			.param("id", "1")
			.param("data", "2025-08-21"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0]", is("14:00:00")))
            .andExpect(jsonPath("$[1]", is("15:00:00")));
	}

	@Test
	void testHorariosOcupadosPaciente() throws Exception {
		String url = "/atendimento/horarios-ocupados-paciente";
		mockMvc.perform(get(url)
			.param("id", "1")
			.param("data", "2025-08-21"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0]", is("14:00:00")));
	}

}
