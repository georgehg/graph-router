package com.avenuecode.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface QueryRepository<T, ID extends Serializable> extends Repository<T, ID> {
	
	T getOne(ID id);

    Optional<T> findById(ID id);
    
    <P> Optional<P> findById(ID id, Class<P> type);

    List<T> findAll();

}

