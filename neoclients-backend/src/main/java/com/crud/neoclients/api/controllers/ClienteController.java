package com.crud.neoclients.api.controllers;

import com.crud.neoclients.api.controllers.response.Response;
import com.crud.neoclients.api.models.Cliente;
import com.crud.neoclients.api.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Cliente>> save(@RequestBody Cliente cliente) {
        Response<Cliente> response = new Response<>();
        try {
            Cliente result = service.saveOrUpdate(cliente);
            response.setData(result);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Cliente>> update(@PathVariable("id") Long id, @RequestBody Cliente cliente) {
        Response<Cliente> response = new Response<>();
        try {
            if (service.findById(id) == null) {
                response.getErrors().add("Cliente não encontrado para atualização");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            cliente.setId(id);
            service.saveOrUpdate(cliente);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Response<List<Cliente>>> getAll() {
        Response<List<Cliente>> response = new Response<>();
        try {
            List<Cliente> result = service.findAll();
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response<Cliente>> getById(@PathVariable("id") Long id) {
        Response<Cliente> response = new Response<>();
        try {
            Cliente result = service.findById(id);
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Cliente>> delete(@PathVariable("id") Long id) {
        Response<Cliente> response = new Response<>();
        try {
            if (service.findById(id) == null) {
                response.getErrors().add("Cliente não encontrado para remoção");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            service.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
