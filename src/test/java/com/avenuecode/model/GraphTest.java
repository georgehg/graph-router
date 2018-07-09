package com.avenuecode.model;

import static com.avenuecode.TestUtils.createGraph;
import static com.avenuecode.TestUtils.createNodes;
import static com.avenuecode.TestUtils.updateGraph;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.avenuecode.exceptions.InvalidRouteException;

public class GraphTest {
    
    @Test
    public void shouldCreateGraph() {
    	List<Node> nodes = createNodes();
    	
    	// Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 2l);
        Edge edge3 = Edge.of(nodes.get(1), nodes.get(2), 5l);
        Edge edge4 = Edge.of(nodes.get(1), nodes.get(0), 5l);
        
        Graph graph = createGraph(Arrays.asList(edge1, edge2, edge3, edge4));
        assertThat(graph.getRoutes()).containsExactlyInAnyOrder(edge1, edge2, edge3, edge4);
    }

    @Test
    public void shouldReplaceGraphRoutes() {
        List<Node> nodes = createNodes();

        // Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 3l);

        // Create and assert new Graph
        Graph graph = createGraph(Arrays.asList(edge1, edge2));
        assertThat(graph.getRoutes()).containsExactlyInAnyOrder(edge1, edge2);

        // Create new Edges for replacing
        Edge edge3 = Edge.of(nodes.get(2), nodes.get(3), 8l);
        Edge edge4 = Edge.of(nodes.get(4), nodes.get(1), 5l);

        // Replace routes with new Edges
        graph = updateGraph(graph, Arrays.asList(edge3, edge4));
        assertThat(graph.getRoutes()).containsExactlyInAnyOrder(edge3, edge4);
    }

    @Test
    public void shouldIssueError_RepeatedRoute() {
        List<Node> nodes = createNodes();

        // Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 3l);
        Edge edge3 = Edge.of(nodes.get(1), nodes.get(2), 3l);

        // Create and assert new Graph
        assertThatThrownBy(() -> createGraph(Arrays.asList(edge1, edge2, edge3)))
        .isInstanceOf(InvalidRouteException.class);
    }

    @Test
    public void shouldIssueError_SameRoute() {
    	 List<Node> nodes = createNodes();

         // Create new edges
         Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
         Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 3l);
         Edge edge3 = Edge.of(nodes.get(0), nodes.get(1), 6l);

         // Create and assert new Graph
         assertThatThrownBy(() -> createGraph(Arrays.asList(edge1, edge2, edge3)))
         .isInstanceOf(InvalidRouteException.class);
    }

}