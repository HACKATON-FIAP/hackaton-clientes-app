package br.com.fiap.usuarios.api.controller;

import br.com.fiap.usuarios.api.exception.ClienteException;
import br.com.fiap.usuarios.api.model.Cliente;
import br.com.fiap.usuarios.api.service.ClienteService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void shouldCreateCliente() throws Exception {
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

        Mockito.when(clienteService.saveCliente(Mockito.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(clienteJson));
    }

    @Test
    void shouldReturn500WhenCpfAlreadyRegistered() throws Exception {
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

        doThrow(new ClienteException("Cliente existente com o CPF: " + cliente.getCpf())).when(clienteService).saveCliente(any(Cliente.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cliente/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().string("Cliente existente com o CPF: " + cliente.getCpf()))
                .andDo(MockMvcResultHandlers.print());
    }

}

