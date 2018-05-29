package br.radixeng;

import br.radixeng.model.Edge;
import br.radixeng.model.Graph;
import br.radixeng.model.Node;
import br.radixeng.model.Router;
import br.radixeng.repository.GraphCommandRepository;
import br.radixeng.repository.NodeQueryRepository;
import br.radixeng.service.RouteServices;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteIT extends AbstractIntegrationTest {

    @Autowired
    private RouteServices routeServices;

    @Autowired
    private NodeQueryRepository nodeQueryRepo;

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

    @Test
    @Transactional
    public void shouldReturnAllRoutes() {
        List<Edge> edges =  createRoutes();
        Graph graph = createGraph(edges);
        assertThat(graph.getEdges()).containsAll(edges);

        // 1 Stop
        Node sourceNode = nodeQueryRepo.findByName("A");
        Node targetNode = nodeQueryRepo.findByName("C");

        Router directRouter = Router.of(sourceNode);
        routeServices.loadRoutes(null, directRouter, sourceNode.getId(), targetNode, 1);
        directRouter.calculatePossibleRoutes();
        assertThat(directRouter.getSimpleRoutes()).contains(new Router.SimpleRoute("AC", 1));

        // 2 stops
        Node distantTargetNode = nodeQueryRepo.findByName("D");

        Router sourceRouter = Router.of(sourceNode);
        routeServices.loadRoutes(null, sourceRouter, sourceNode.getId(), distantTargetNode, 3);
        sourceRouter.calculatePossibleRoutes();
        assertThat(sourceRouter.getSimpleRoutes()).contains(new Router.SimpleRoute("ACD", 2));
    }
}
