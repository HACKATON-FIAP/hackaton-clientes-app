package br.com.fiap.usuarios.api.service;


import br.com.fiap.usuarios.api.exception.ClienteException;
import br.com.fiap.usuarios.api.model.Cliente;
import br.com.fiap.usuarios.api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteServiceIntegrationTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

     private final static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("db_clientes")
            .withUsername("postgres")
            .withPassword("teste123");

    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @BeforeEach
    public void setUp() {
        clienteRepository.deleteAll();
    }


    @Test
    public void testSaveCliente_success() {
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

        Cliente savedCliente = clienteService.saveCliente(cliente);

        assertThat(savedCliente).isNotNull();
        assertThat(savedCliente.getId()).isNotNull();
        assertThat(savedCliente.getCpf()).isEqualTo("11111111111");
    }

    @Test
    public void testSaveCliente_existingCpf_throwsException() {
        Cliente cliente1 = new Cliente();
        cliente1.setCpf("11111111111");
        cliente1.setNome("Jefferson");
        cliente1.setEmail("jeffersantos@gmail.com");
        cliente1.setTelefone("+55 11 91234-5678");
        cliente1.setRua("Rua A");
        cliente1.setCidade("São Paulo");
        cliente1.setEstado("São Paulo");
        cliente1.setCep("01111-123");
        cliente1.setPais("Brasil");
        clienteService.saveCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setCpf("11111111111");
        cliente2.setNome("Ricardo");
        cliente2.setEmail("jeffersantos@gmail.com");
        cliente2.setTelefone("+55 11 91234-5678");
        cliente2.setRua("Rua A");
        cliente2.setCidade("São Paulo");
        cliente2.setEstado("São Paulo");
        cliente2.setCep("01111-123");
        cliente2.setPais("Brasil");

        Throwable thrown = catchThrowable(() -> clienteService.saveCliente(cliente2));

        assertThat(thrown).isInstanceOf(ClienteException.class)
                .hasMessageContaining("Cliente existente com o CPF: 11111111111");
    }


}
