package com.crud.neoclients.api.services.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IGenericService<T, ID extends Serializable> {

    T saveOrUpdate(T object) throws Exception;

    void delete(Long id) throws Exception;

    T findById(Long id) throws Exception;

    List<T> findAll() throws Exception;
}
