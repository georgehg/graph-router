package br.radixeng.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface QueryRepository<T, ID extends Serializable> extends Repository<T, ID> {

    T getOne(ID var1);

    Optional<T> findOne(ID var1);

    List<T> findAll();

    boolean exists(ID var1);

}

