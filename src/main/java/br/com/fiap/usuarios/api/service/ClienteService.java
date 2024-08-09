package br.com.fiap.usuarios.api.service;

import br.com.fiap.usuarios.api.exception.ClienteException;
import br.com.fiap.usuarios.api.model.Cliente;
import br.com.fiap.usuarios.api.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente saveCliente(Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findByCpf(cliente.getCpf());

        if (clienteExistente.isPresent()) {
            throw new ClienteException("Cliente existente com o CPF: " + cliente.getCpf());
        }
        return clienteRepository.save(cliente);
    }

    public boolean verifyCpf(String cpf) {
        boolean consultaCPF = clienteRepository.existsByCpf(cpf);
        if(consultaCPF){
            return true;
        }else{
            return false;
        }
    }





}
