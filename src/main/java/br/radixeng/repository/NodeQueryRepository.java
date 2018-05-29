package br.radixeng.repository;

import br.radixeng.model.Node;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface NodeQueryRepository extends QueryRepository<Node, Long> {

    Node findByName(String name);

    @Query(value = "select target_id, distance from edge where source_id = ?1 and target_id = ?2", nativeQuery = true)
    Set<Object[]> findAdjacentNodes(Long sourceId, Long targetId);

    @Query(value = "select target_id, distance from edge where graph_id = ?1 and source_id = ?2 and target_id = ?3", nativeQuery = true)
    Set<Object[]> findAdjacentNodes(Long graphId, Long sourceId, Long targetId);

    @Query(value = "select target_id, distance from edge where source_id = ?1 and target_id != ?2", nativeQuery = true)
    Set<Object[]> findDirectNodes(Long sourceId, Long startId);

    @Query(value = "select target_id, distance from edge where graph_id = ?1 and source_id = ?2 and target_id != ?3", nativeQuery = true)
    Set<Object[]> findDirectNodes(Long graphId, Long sourceId, Long startId);

}
