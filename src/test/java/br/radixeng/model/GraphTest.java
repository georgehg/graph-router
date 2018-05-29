package br.radixeng.model;

import br.radixeng.repository.GraphCommandRepository;
import br.radixeng.repository.NodeQueryRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphTest extends AbstractRepositoryTests {

    @Autowired
    private GraphCommandRepository graphCommandRepo;

    private List<Node> createNodes() {
        return Arrays.asList(
                Node.of("A"),
                Node.of("B"),
                Node.of("C"),
                Node.of("D"));
    }

    private List<Edge> createRoutes() {
        List<Node> nodes = createNodes();

        return Arrays.asList(
                Edge.of(nodes.get(0), nodes.get(1), 6l),
                Edge.of(nodes.get(0), nodes.get(2), 9l),
                Edge.of(nodes.get(0), nodes.get(2), 5l),
                Edge.of(nodes.get(0), nodes.get(3), 10l),
                Edge.of(nodes.get(1), nodes.get(0), 6l),
                Edge.of(nodes.get(1), nodes.get(2), 4l),
                Edge.of(nodes.get(1), nodes.get(3), 5l),
                Edge.of(nodes.get(1), nodes.get(3), 7l),
                Edge.of(nodes.get(2), nodes.get(0), 3l),
                Edge.of(nodes.get(2), nodes.get(3), 8l));
    }

    @After
    public void cleanDataBase() {
        graphCommandRepo.deleteAll();
    }

    private Graph createGraph(List<Edge> edges) {
        Graph newGraph = Graph.of(edges);
        return graphCommandRepo.save(newGraph);
    }

    private Graph updateGraph(Graph graph, Edge... edges) {
        graph.addRoutes(Arrays.asList(edges));
        return graphCommandRepo.save(graph);
    }

    @Test
    public void shouldCreateAndPersistGraphs() {
        List<Edge> edges =  createRoutes();
        Graph graph = createGraph(edges);
        System.out.println(graph);
        assertThat(graph.getEdges()).containsAll(edges);
    }

    @Test
    public void shouldReplaceRoutesForGraph() {
        List<Node> nodes = createNodes();

        // Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 3l);

        // Create and assert new Graph
        Graph graph = createGraph(Arrays.asList(edge1, edge2));
        assertThat(graph.getEdges()).containsExactlyInAnyOrder(edge1, edge2);

        // Create new Edges for replacing
        Edge edge3 = Edge.of(nodes.get(0), nodes.get(1), 8l);
        Edge edge4 = Edge.of(nodes.get(1), nodes.get(2), 5l);

        // Replace routes with new Edges
        graph = updateGraph(graph, edge3, edge4);
        System.out.println(graph);

        assertThat(graph.getEdges()).containsExactlyInAnyOrder(edge3, edge4);
    }

    @Test
    public void shouldReplaceRepeatedRouteForGraph() {
        List<Node> nodes = createNodes();

        // Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 3l);

        // Create and assert new Graph
        Graph graph = createGraph(Arrays.asList(edge1, edge2));
        assertThat(graph.getEdges()).containsExactlyInAnyOrder(edge1, edge2);

        // Create Repeated Edge for replacing
        Edge edge3 = Edge.of(nodes.get(1), nodes.get(2), 3l);

        // Replace routes with new Edges
        graph = updateGraph(graph, edge1, edge2, edge3);
        System.out.println(graph);

        assertThat(graph.getEdges()).containsOnly(edge1, edge3);
    }

    @Test
    public void shouldAddRoutesForGraph() {

        List<Node> nodes = createNodes();

        // Create new edges
        Edge edge1 = Edge.of(nodes.get(0), nodes.get(1), 6l);
        Edge edge2 = Edge.of(nodes.get(1), nodes.get(2), 3l);

        // Create and assert new Graph
        Graph graph = createGraph(Arrays.asList(edge1, edge2));
        assertThat(graph.getEdges()).containsExactlyInAnyOrder(edge1, edge2);

        // Create new Edges for Adding
        Edge edge3 = Edge.of(nodes.get(1), nodes.get(0), 8l);
        Edge edge4 = Edge.of(nodes.get(2), nodes.get(1), 5l);

        // Replace routes with new Edges
        graph = updateGraph(graph, edge1, edge2, edge3, edge4);
        System.out.println(graph);

        assertThat(graph.getEdges()).containsExactlyInAnyOrder(edge1, edge2, edge3, edge4);
    }

}