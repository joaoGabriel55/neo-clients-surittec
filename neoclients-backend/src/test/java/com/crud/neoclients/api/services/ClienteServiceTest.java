package com.crud.neoclients.api.services;

import com.crud.neoclients.api.NeoclientsApplicationTests;
import com.crud.neoclients.api.models.Cliente;
import com.crud.neoclients.api.models.Email;
import com.crud.neoclients.api.models.Endereco;
import com.crud.neoclients.api.models.Telefone;
import com.crud.neoclients.api.models.enums.TipoTelefone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ClienteServiceTest extends NeoclientsApplicationTests {

    @Autowired
    private ClienteService clienteService;

    private Cliente mockCliente() {
        Cliente cliente = new Cliente();

        cliente.setNome("John");
        cliente.setCpf("215.905.880-70");

        Email email = new Email();
        email.setCliente(cliente);
        email.setEmailAddress("j.email@test.com");
        Set<Email> emails = new HashSet<>();
        emails.add(email);
        cliente.setEmails(emails);

        Telefone telefone = new Telefone();
        telefone.setCliente(cliente);
        telefone.setNumero("(84) 99999-9999");
        telefone.setTipoTelefone(TipoTelefone.CELULAR);

        Telefone telefone2 = new Telefone();
        telefone2.setCliente(cliente);
        telefone2.setNumero("(84) 9999-9999");
        telefone2.setTipoTelefone(TipoTelefone.RESIDENCIAL);

        Set<Telefone> telefones = new HashSet<>();
        telefones.add(telefone);
        telefones.add(telefone2);
        cliente.setTelefones(telefones);

        Endereco endereco = new Endereco();
        endereco.setCep("59151-000");
        endereco.setLogradouro("Rua Legal");
        endereco.setBairro("Ace");
        endereco.setCidade("Natal");
        endereco.setUf("RN");
        endereco.setCliente(cliente);
        cliente.setEndereco(endereco);
        return cliente;
    }

    @Test
    public void createTest() throws Exception {
        Cliente clienteSaved = clienteService.saveOrUpdate(mockCliente());
        assertNotNull(clienteSaved);
        clienteService.delete(clienteSaved.getId());
    }

    @Test
    public void updateTest() throws Exception {
        Cliente clienteSaved = clienteService.saveOrUpdate(mockCliente());
        assertNotNull(clienteSaved);

        clienteSaved.setNome("Lion");
        Cliente clienteUpdated = clienteService.saveOrUpdate(clienteSaved);
        assertEquals("Lion", clienteUpdated.getNome());
        clienteService.delete(clienteSaved.getId());
    }

    @Test
    public void findAllTest() throws Exception {
        Cliente cliente = clienteService.saveOrUpdate(mockCliente());
        List<Cliente> collection = clienteService.findAll();
        assertNotNull(collection);
        clienteService.delete(cliente.getId());
    }


    @Test
    public void findByIdTest() throws Exception {
        Cliente clienteSaved = clienteService.saveOrUpdate(mockCliente());
        Cliente clienteFound = clienteService.findById(clienteSaved.getId());
        assertNotNull(clienteFound);
        clienteService.delete(clienteSaved.getId());
    }

    @Test
    public void deleteByIdTest() throws Exception {
        Cliente clienteSaved = clienteService.saveOrUpdate(mockCliente());
        clienteService.delete(clienteSaved.getId());
        Cliente clienteFound = clienteService.findById(clienteSaved.getId());
        assertNull(clienteFound);
    }

}
