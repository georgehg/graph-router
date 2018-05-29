package br.radixeng.repository;

import br.radixeng.model.Graph;

public interface GraphQueryRepository extends QueryRepository<Graph, Long> {

    <T> T findById(Long id, Class<T> type);

}
