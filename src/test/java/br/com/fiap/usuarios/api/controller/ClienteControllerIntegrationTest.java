package br.com.fiap.usuarios.api.controller;

import br.com.fiap.usuarios.api.model.Cliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("db_usuarios")
            .withUsername("postgres")
            .withPassword("teste123");

    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }


    @Test
    public void shouldCreateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(100L);
        cliente.setCpf("11111111111");
        cliente.setNome("Jefferson");
        cliente.setEmail("jeffersantos@gmail.com");
        cliente.setTelefone("+55 11 91234-5678");
        cliente.setRua("Rua A");
        cliente.setCidade("São Paulo");
        cliente.setEstado("São Paulo");
        cliente.setCep("01111-123");
        cliente.setPais("Brasil");

        String clienteJson = objectMapper.writeValueAsString(cliente);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/cliente/create")
                        .contentType("application/json")
                        .content(clienteJson))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldReturnBadRequestForInvalidCliente() throws Exception {

        Cliente invalidCliente = new Cliente();

        invalidCliente.setNome("Jefferson");
        invalidCliente.setEmail("invalid-email");

        String clienteJson = objectMapper.writeValueAsString(invalidCliente);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/cliente/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isBadRequest())
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseContent);

        assertNotNull(responseContent);
    }

}