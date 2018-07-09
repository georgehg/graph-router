package com.avenuecode.model;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.avenuecode.repository.NodeQueryRepository;

import lombok.ToString;

@ToString
public class Route {

	private NodeQueryRepository nodeQueryRepo;

    private final Node head;

    private final Long distance;

    private List<Route> adjacentNodes = new LinkedList<>();

    private Route(Node head, Long distance) {
        this.head = head;
        this.distance = distance;
        this.nodeQueryRepo = ContextProvider.getBean("nodeQueryRepository", NodeQueryRepository.class);
    }

    public static Route head(Node head) {
        return new Route(head, 0l);
    }

    public static Route of(Node head, Long distance) {
        return new Route(head, distance);
    }
    
    public Node getHead() {
        return this.head;
    }
    
    public Long getDistance() {
        return this.distance;
    }

    public List<Route> getAdjacentNodes() {
        return Collections.unmodifiableList(adjacentNodes);
    }

    public void startRouting(Long graphId, Node target) {
        adjacentNodes.clear();
        findRoutes(graphId, target, null, 0);
    }

    public void startRouting(Long graphId, Node target, int maxStops) {
        adjacentNodes.clear();
        findRoutes(graphId, target, null, maxStops);
    }
    
    private void findRoutes(Long graphId, Node target, List<Node> lastVisited, int maxStops) {
        if (maxStops == 1) {
            adjacentNodes.addAll(
                    findDirectNodes(graphId, head, target)
                            .stream()
                            .map(route -> Route.of(nodeQueryRepo.getOne(Long.valueOf(route[0].toString())),
                                                    Long.valueOf(route[1].toString())))
                            .collect(Collectors.toList()));
            return;
        }

        final List<Node> visited = new ArrayList<>();
        if (!Objects.isNull(lastVisited)) {
        	if (head.equals(target)) return;
            visited.addAll(lastVisited);
        }
        
        adjacentNodes.addAll(
            findAllAdjacentNodes(graphId, head, visited)
                    .stream()
                    .map(route -> Route.of(nodeQueryRepo.getOne(Long.valueOf(route[0].toString())),
                                            Long.valueOf(route[1].toString())))
                    .map(route -> {
                    	if (!Objects.isNull(lastVisited)) visited.add(this.head);
                        route.findRoutes(graphId, target, visited, maxStops - 1);
                        return route;
                    })
                    .filter(route -> route.getHead().equals(target) || !route.getAdjacentNodes().isEmpty())
                    .collect(Collectors.toList()));
    }
    
    private Set<Object[]> findAllAdjacentNodes(Long graphId, Node source, List<Node> visited) {
    	Set<Long> vistiedList = visited.stream().map(node -> node.getId()).collect(toSet());
    	
        if (graphId == null) {
            return nodeQueryRepo.findAdjacentNodes(source.getId(), vistiedList);
        } else {
            return nodeQueryRepo.findAdjacentNodes(graphId, source.getId(), vistiedList);
        }
    }

    private Set<Object[]> findDirectNodes(Long graphId, Node source, Node target) {
        if (graphId == null) {
            return nodeQueryRepo.findDirectNodes(source.getId(), target.getId());
        } else {
            return nodeQueryRepo.findDirectNodes(graphId, source.getId(), target.getId());
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if( (obj == null) || !(obj instanceof Route) ) {
            return false;
        }

        Route other = (Route) obj;
        
        if (!Objects.equals(head, other.head)) return false;
        return Objects.equals(distance, other.distance);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + head.hashCode();
		result = prime * result + distance.hashCode();
		return result;
	}

} 
