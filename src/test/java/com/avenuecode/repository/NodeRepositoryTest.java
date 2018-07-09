package com.avenuecode.repository;

import static com.avenuecode.TestUtils.createNodes;
import static com.avenuecode.TestUtils.createOtherRoutes;
import static com.avenuecode.TestUtils.createRoutes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.dao.DataIntegrityViolationException;

import com.avenuecode.AbstractRepositoryTests;
import com.avenuecode.model.Edge;
import com.avenuecode.model.Graph;
import com.avenuecode.model.Node;
import com.avenuecode.model.Route;

public class NodeRepositoryTest extends AbstractRepositoryTests {

    @Test
    public void shouldCreateAndPersistNodes() {
        Node newNodeA = Node.of("A");
        Node newNodeB = Node.of("B");

        nodeCommandRepo.save(newNodeA);
        nodeCommandRepo.save(newNodeB);
        
        Node nodeA = nodeQueryRepo.getOne(newNodeA.getId());
        Node nodeB = nodeQueryRepo.getOne(newNodeB.getId());
        
        assertThat(nodeA.getId()).isNotNull();
        assertThat(nodeA.getName()).isEqualTo("A");
        
        assertThat(nodeB.getId()).isNotNull();
        assertThat(nodeB.getName()).isEqualTo("B");
    }
    
    @Test
    public void shouldPersistAndFindNodes() {
        Node nodeA = Node.of("A");
        Node nodeB = Node.of("B");

        nodeCommandRepo.save(nodeA);
        nodeCommandRepo.save(nodeB);
    
        assertThat(nodeQueryRepo.findByName("A")).isNotNull();
        assertThat(nodeQueryRepo.findByName("B")).isNotNull();
    }

    @Test
    public void shouldFail_RepeatedNode() {
        nodeCommandRepo.save(Node.of("A"));

        assertThatThrownBy(() -> nodeCommandRepo.save(Node.of("A")))
        .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void shouldFail_NullValue() {
        assertThatThrownBy(() -> nodeCommandRepo.save(Node.of(null)))
        .isInstanceOf(ConstraintViolationException.class);
    }
    
    @Test
    public void shouldReturnAdjacentNodesFromGraph() {
    	List<Node> nodes = createNodes();
        List<Edge> routes =  createRoutes(nodes);
        Graph newGraph = saveGraph(routes);
        
        Long sourceId = nodeQueryRepo.findByName("A").getId();
        Set<Long> visited = Sets.newSet(nodeQueryRepo.findByName("A").getId());
        
        List<Route> adjacentNodes =
        		nodeQueryRepo.findAdjacentNodes(newGraph.getId(), sourceId, visited)
		        			.stream()
		        			.map(route -> Route.of(nodeQueryRepo.getOne(Long.valueOf(route[0].toString())),
		                							Long.valueOf(route[1].toString())))
		        			.collect(Collectors.toList());
        
        assertThat(adjacentNodes)
        	.containsOnly(Route.of(nodeQueryRepo.findByName("B"), 5l),
        				  Route.of(nodeQueryRepo.findByName("D"), 5l),
        				  Route.of(nodeQueryRepo.findByName("E"), 7l));
    }
    
    @Test
    public void shouldReturnAdjacentNodesFromGraph_WithVisited() {
    	List<Node> nodes = createNodes();
        Graph newGraph = saveGraph(createOtherRoutes(nodes));
        
        Long sourceId = nodeQueryRepo.findByName("B").getId();
        Set<Long> visited = Sets.newSet(nodeQueryRepo.findByName("A").getId(),
        								nodeQueryRepo.findByName("C").getId());
        
        List<Route> adjacentNodes =
        		nodeQueryRepo.findAdjacentNodes(newGraph.getId(), sourceId, visited)
		        			.stream()
		        			.map(route -> Route.of(nodeQueryRepo.getOne(Long.valueOf(route[0].toString())),
		                							Long.valueOf(route[1].toString())))
		        			.collect(Collectors.toList());
        
        assertThat(adjacentNodes)
        	.containsOnly(Route.of(nodeQueryRepo.findByName("D"), 4l),
        				  Route.of(nodeQueryRepo.findByName("D"), 8l));
    }
    
    @Test
    public void shouldReturnAdjacentNodesFromAllGraphs() {
    	List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));
        saveGraph(createOtherRoutes(nodes));
       
        Long sourceId = nodeQueryRepo.findByName("A").getId();
        Set<Long> visited = Sets.newSet(nodeQueryRepo.findByName("A").getId());
        
        List<Route> adjacentNodes =
        		nodeQueryRepo.findAdjacentNodes(sourceId, visited)
		        			.stream()
		        			.map(route -> Route.of(nodeQueryRepo.getOne(Long.valueOf(route[0].toString())),
		                							Long.valueOf(route[1].toString())))
		        			.collect(Collectors.toList());
        
        assertThat(adjacentNodes)
        	.containsOnly(Route.of(nodeQueryRepo.findByName("B"), 6l),
        				  Route.of(nodeQueryRepo.findByName("E"), 4l),
        				  Route.of(nodeQueryRepo.findByName("B"), 5l),
        				  Route.of(nodeQueryRepo.findByName("D"), 5l),
        				  Route.of(nodeQueryRepo.findByName("E"), 7l));
    }
    
    @Test
    public void shouldReturnAdjacentNodesFromAllGraphs_WithVisited() {
    	List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));
        saveGraph(createOtherRoutes(nodes));
       
        Long sourceId = nodeQueryRepo.findByName("C").getId();
        Set<Long> visited = Sets.newSet(nodeQueryRepo.findByName("A").getId(),
        								nodeQueryRepo.findByName("B").getId());
        
        List<Route> adjacentNodes =
        		nodeQueryRepo.findAdjacentNodes(sourceId, visited)
		        			.stream()
		        			.map(route -> Route.of(nodeQueryRepo.getOne(Long.valueOf(route[0].toString())),
		                							Long.valueOf(route[1].toString())))
		        			.collect(Collectors.toList());
        
        assertThat(adjacentNodes)
        	.containsOnly(Route.of(nodeQueryRepo.findByName("D"), 8l),
        				  Route.of(nodeQueryRepo.findByName("E"), 2l),
        				  Route.of(nodeQueryRepo.findByName("D"), 1l),
        				  Route.of(nodeQueryRepo.findByName("E"), 7l));
    }

}