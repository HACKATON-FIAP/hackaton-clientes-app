package br.com.fiap.usuarios.api.service;

import br.com.fiap.usuarios.api.exception.ClienteException;
import br.com.fiap.usuarios.api.model.Cliente;
import br.com.fiap.usuarios.api.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveCliente() {
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


        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente clienteSalvo = clienteService.saveCliente(cliente);

        assertEquals("Jefferson", clienteSalvo.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testSaveClienteExistingCliente() {

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
        // Arrange
        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        // Act & Assert
        ClienteException thrown = assertThrows(ClienteException.class, () -> {
            clienteService.saveCliente(cliente);
        });

        assertEquals("Cliente existente com o CPF: " + cliente.getCpf(), thrown.getMessage());
        verify(clienteRepository, times(1)).findByCpf(cliente.getCpf());
        verify(clienteRepository, never()).save(cliente);
    }


}