package br.com.fiap.usuarios.api.controller;

import br.com.fiap.usuarios.api.exception.ClienteException;
import br.com.fiap.usuarios.api.model.Cliente;
import br.com.fiap.usuarios.api.service.ClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cliente")
@CrossOrigin("*")
public class ClienteController {

    private ClienteService clienteService;

    @PostMapping("/create")
    public ResponseEntity<Object> createCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        Cliente savedCliente = clienteService.saveCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
    }


    @GetMapping("/validarCPF/{cpf}")
    public ResponseEntity<String> validarCpf(@PathVariable String cpf) {
        boolean cpfExists = clienteService.verifyCpf(cpf);
        try {
            if (!cpfExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF inválido");
            }
            return ResponseEntity.status(HttpStatus.OK).body("CPF válido");
        } catch (ClienteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
