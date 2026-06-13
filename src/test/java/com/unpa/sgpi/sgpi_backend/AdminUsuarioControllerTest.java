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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarUsuariosComoAdmin() throws Exception {
        mockMvc.perform(get("/api/admin/usuarios")
                        .header("Authorization", bearer(login("admin", "Admin123!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claveTrabajador").exists());
    }

    @Test
    void listarUsuariosComoInvestigador() throws Exception {
        mockMvc.perform(get("/api/admin/usuarios")
                        .header("Authorization", bearer(login("inv001", "Investigador123!"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void asignarRolesComoAdmin() throws Exception {
        Long usuarioId = obtenerUsuarioId("inv002");

        mockMvc.perform(put("/api/admin/usuarios/roles")
                        .header("Authorization", bearer(login("admin", "Admin123!")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuarioId": %d, "roles": ["INVESTIGADOR","DIRECTOR"]}
                                """.formatted(usuarioId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles[0]").value("DIRECTOR"))
                .andExpect(jsonPath("$.roles[1]").value("INVESTIGADOR"));
    }

    @Test
    void asignarRolesSinAutenticacion() throws Exception {
        mockMvc.perform(put("/api/admin/usuarios/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuarioId": 1, "roles": ["INVESTIGADOR"]}
                                """))
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

    private Long obtenerUsuarioId(String clave) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/admin/usuarios")
                        .header("Authorization", bearer(login("admin", "Admin123!"))))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode usuarios = objectMapper.readTree(result.getResponse().getContentAsString());
        for (JsonNode usuario : usuarios) {
            if (clave.equals(usuario.get("claveTrabajador").asText())) {
                return usuario.get("id").asLong();
            }
        }
        throw new IllegalStateException("Usuario no encontrado en el fixture");
    }
}
