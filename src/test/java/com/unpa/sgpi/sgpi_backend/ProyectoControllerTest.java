package com.unpa.sgpi.sgpi_backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void directorVeSoloProyectosDeSuInstituto() throws Exception {
        mockMvc.perform(get("/api/proyectos")
                        .header("Authorization", bearer(login("dir001", "Director123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].institutoNombre").value("Instituto de Biotecnología"));
    }

    @Test
    void investigadorVeSoloSusProyectos() throws Exception {
        mockMvc.perform(get("/api/proyectos")
                        .header("Authorization", bearer(login("inv001", "Investigador123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Biofertilizantes para cultivos regionales"));
    }

    @Test
    void adminVeTodosLosProyectos() throws Exception {
        mockMvc.perform(get("/api/proyectos")
                        .header("Authorization", bearer(login("admin", "Admin123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void sinAutenticacionEsRechazado() throws Exception {
        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isForbidden());
    }

    private String login(String clave, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"claveTrabajador":"%s","password":"%s"}
                                """.formatted(clave, password)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("token").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
