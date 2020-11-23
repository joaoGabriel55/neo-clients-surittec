package com.crud.neoclients.api.services.interfaces;

import com.crud.neoclients.api.models.Cliente;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public interface IClienteService extends IGenericService<Cliente, Serializable> {
}
