package com.avenuecode.model;

import static com.avenuecode.TestUtils.createNodes;
import static com.avenuecode.TestUtils.createRoutes;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.context.annotation.Import;

import com.avenuecode.AbstractRepositoryTests;
import com.avenuecode.ContextAware;

@Import(ContextAware.class)
public class RouteTest extends AbstractRepositoryTests {

    @Test
    public void loadRoutesUnlimnitedStops() throws Exception {
        List<Edge> routes =  createRoutes(createNodes());
        Graph newGraph = saveGraph(routes);

        Route head = Route.head(nodeQueryRepo.findByName("A"));
        Node target = nodeQueryRepo.findByName("C");

        head.startRouting(newGraph.getId(), target);

        assertThat(head.getAdjacentNodes())
                .contains(Route.of(Node.of("B"), 5L),
                          Route.of(Node.of("D"), 5L),
                          Route.of(Node.of("E"), 7L));
    }

    @Test
    public void loadRoutesMaxStops() throws Exception {
        List<Edge> routes =  createRoutes(createNodes());
        Graph newGraph = saveGraph(routes);

        Route head = Route.head(nodeQueryRepo.findByName("A"));
        Node target = nodeQueryRepo.findByName("C");

        head.startRouting(newGraph.getId(), target, 3);

        assertThat(head.getAdjacentNodes())
                .contains(Route.of(Node.of("B"), 5L),
                          Route.of(Node.of("D"), 5L),
                          Route.of(Node.of("E"), 7L));
    }

}