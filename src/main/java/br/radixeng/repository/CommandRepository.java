package br.radixeng.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

@NoRepositoryBean
public interface CommandRepository<T, ID extends Serializable> extends Repository<T, ID> {

    <S extends T> S save(S var1);

    void flush();

    <S extends T> Iterable<S> save(Iterable<S> var1);

    void delete(ID var1);

    void delete(T var1);

    void delete(Iterable<? extends T> var1);

    void deleteAll();

}