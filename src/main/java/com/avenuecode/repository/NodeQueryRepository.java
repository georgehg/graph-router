package com.avenuecode.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avenuecode.model.Node;

public interface NodeQueryRepository extends QueryRepository<Node, Long> {

    Node findByName(String name);

    @Query(value = "select target_id, distance from route where source_id = ?1 and target_id not in ?2", nativeQuery = true)
    Set<Object[]> findAdjacentNodes(Long sourceId, Set<Long> visited);

    @Query(value = "select target_id, distance from route where graph_id = ?1 and source_id = ?2 and target_id not in ?3", nativeQuery = true)
    Set<Object[]> findAdjacentNodes(Long graphId, Long sourceId, Set<Long> visited);

    @Query(value = "select target_id, distance from route where source_id = ?1 and target_id = ?2", nativeQuery = true)
    Set<Object[]> findDirectNodes(Long sourceId, Long targetId);

    @Query(value = "select target_id, distance from route where graph_id = ?1 and source_id = ?2 and target_id = ?3", nativeQuery = true)
    Set<Object[]> findDirectNodes(Long graphId, Long sourceId, Long targetId);

}