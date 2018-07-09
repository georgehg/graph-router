package com.avenuecode.service;

import static com.avenuecode.TestUtils.createNodes;
import static com.avenuecode.TestUtils.createOtherRoutes;
import static com.avenuecode.TestUtils.createRoutes;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.context.annotation.Import;

import com.avenuecode.AbstractRepositoryTests;
import com.avenuecode.ContextAware;
import com.avenuecode.model.Graph;
import com.avenuecode.model.Node;
import com.avenuecode.model.Router;

@Import(ContextAware.class)
public class RouterTest extends AbstractRepositoryTests {

    @Test
    public void shouldReturnAllRoutes_FromGraph() throws Exception {
        Graph newGraph = saveGraph(createRoutes(createNodes()));

        Router router = Router.from(newGraph.getId(), nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeQueryRepo.findByName("C"));

        assertThat(router.getSimpleRoutes())
                .contains(new Router.SimpleRoute("ABC", 2),
                          new Router.SimpleRoute("ADC", 2),
                          new Router.SimpleRoute("AEBC", 3),
                          new Router.SimpleRoute("ADEBC", 4));
    }
    
    @Test
    public void shouldReturnAllCircularRoutes_FromGraph() throws Exception {
        Graph newGraph = saveGraph(createRoutes(createNodes()));

        Router router = Router.from(newGraph.getId(), nodeQueryRepo.findByName("C"));
        router.initializeRoutes(nodeQueryRepo.findByName("C"), 3);

        assertThat(router.getSimpleRoutes())
                .contains(new Router.SimpleRoute("CDC", 2),
                          new Router.SimpleRoute("CEBC", 3));
    }

    @Test
    public void shouldReturnRoutesWithMaxStops_FromGraph() throws Exception {
        Graph newGraph = saveGraph(createRoutes(createNodes()));

        Router router = Router.from(newGraph.getId(), nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeQueryRepo.findByName("C"), 4);

        assertThat(router.getSimpleRoutes())
                .contains(new Router.SimpleRoute("ABC", 2),
                        new Router.SimpleRoute("ADC", 2),
                        new Router.SimpleRoute("AEBC", 3),
                        new Router.SimpleRoute("ADEBC", 4));
    }

    @Test
    public void shouldReturnAllRoutes_FromAllGraphs() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));
        saveGraph(createOtherRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("B"));
        router.initializeRoutes(nodeQueryRepo.findByName("D"));

        assertThat(router.getSimpleRoutes())
                .contains(new Router.SimpleRoute("BD", 1),
                        new Router.SimpleRoute("BCD", 2),
                        new Router.SimpleRoute("BAD", 2));
    }

    @Test
    public void shouldReturnRoutesWithMaxStops_FromAllGraphs() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));
        saveGraph(createOtherRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("B"));
        router.initializeRoutes(nodeQueryRepo.findByName("E"), 2);

        assertThat(router.getSimpleRoutes())
                .contains(new Router.SimpleRoute("BAE", 2),
                        new Router.SimpleRoute("BDE", 2),
                        new Router.SimpleRoute("BCE", 2));
    }

    @Test
    public void shouldReturnEmptyRoutes_PathNotFound() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));
        saveGraph(createOtherRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeCommandRepo.save(Node.of("F")));

        assertThat(router.getSimpleRoutes()).isEmpty();
    }

    @Test
    public void shouldReturnPathDistance() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createOtherRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeQueryRepo.findByName("D"));

        assertThat(router.getPathDistance("ABCD")).isEqualTo(9L);
    }
    
    @Test
    public void shouldReturnNoPathDistance() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createOtherRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeQueryRepo.findByName("D"));

        assertThat(router.getPathDistance("ACBD")).isEqualTo(-1L);
    }
    
    @Test
    public void shouldReturnNoPathDistance_FromGraph() throws Exception {
        List<Node> nodes = createNodes();
        Graph newGraph = saveGraph(createOtherRoutes(nodes));

        Router router = Router.from(newGraph.getId(), nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeQueryRepo.findByName("D"));

        assertThat(router.getPathDistance("ACBD")).isEqualTo(-1L);
    }

    @Test
    public void shouldReturnShortestPathDistance() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("A"));
        router.initializeRoutes(nodeQueryRepo.findByName("C"));

        assertThat(router.getShortestPathDistance().getDistance()).isEqualTo(9L);
        assertThat(router.getShortestPathDistance().getRoute()).isEqualTo("ABC");
    }
    
    @Test
    public void shouldReturnShortestPathDistance_SameNode() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("B"));
        router.initializeRoutes(nodeQueryRepo.findByName("B"));

        assertThat(router.getShortestPathDistance().getDistance()).isEqualTo(0L);
        assertThat(router.getShortestPathDistance().getRoute()).isEqualTo("BB");
    }
    
    @Test
    public void shouldReturnNoShortestPathDistance() throws Exception {
        List<Node> nodes = createNodes();
        saveGraph(createRoutes(nodes));

        Router router = Router.from(nodeQueryRepo.findByName("E"));
        router.initializeRoutes(nodeQueryRepo.findByName("A"));

        assertThat(router.getShortestPathDistance().getDistance()).isEqualTo(-1L);
        assertThat(router.getShortestPathDistance().getRoute()).isEqualTo("");
    }

}