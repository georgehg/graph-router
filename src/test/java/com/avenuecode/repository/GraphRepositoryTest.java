package com.avenuecode.repository;

import static com.avenuecode.TestUtils.createNodes;
import static com.avenuecode.TestUtils.createRoutes;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import com.avenuecode.AbstractRepositoryTests;
import org.junit.Test;

import com.avenuecode.model.Edge;
import com.avenuecode.model.Graph;
import com.avenuecode.model.Node;

public class GraphRepositoryTest extends AbstractRepositoryTests {

    private Graph updateGraph(Graph graph, List<Edge> edges) {
        graph.addRoutes(edges);
        return graphCommandRepo.save(graph);
    }

    @Test
    public void shouldCreateAndPersistGraphs() {
    	List<Node> nodes = createNodes();
        List<Edge> routes =  createRoutes(nodes);
        Graph newGraph = saveGraph(routes);
        
        Graph graph = graphQueryRepo.getOne(newGraph.getId());
        assertThat(graph.getRoutes()).containsOnlyElementsOf(routes);
        assertThat(nodeQueryRepo.findAll()).containsOnlyElementsOf(nodes);
    }
    
    @Test
    public void shouldCreateAndUpdateGraphs() {
    	List<Node> nodes = createNodes();
    	
    	// Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 2l);
        Edge edge3 = Edge.of(nodes.get(1), nodes.get(2), 5l);
        Edge edge4 = Edge.of(nodes.get(1), nodes.get(0), 5l);
        
        Graph newGraph = saveGraph(Arrays.asList(edge1, edge2, edge3, edge4));
         
        Graph graph = graphQueryRepo.getOne(newGraph.getId());
        assertThat(graph.getRoutes()).containsExactlyInAnyOrder(edge1, edge2, edge3, edge4);        
        
        List<Edge> routes =  createRoutes(nodes);
        Graph updatedGraph = updateGraph(newGraph, routes);
        assertThat(updatedGraph.getRoutes()).containsOnlyElementsOf(routes);
        assertThat(nodeQueryRepo.findAll()).containsOnlyElementsOf(nodes);
    }

}