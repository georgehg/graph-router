package com.avenuecode;

import java.util.Arrays;
import java.util.List;

import com.avenuecode.model.Edge;
import com.avenuecode.model.Graph;
import com.avenuecode.model.Node;

public class TestUtils {
	
	public static List<Node> createNodes() {
        return Arrays.asList(
                Node.of("A"),//0
                Node.of("B"),//1
                Node.of("C"),//2
                Node.of("D"),//3
                Node.of("E"));//4
    }

    public static List<Edge> createRoutes(List<Node> nodes) {
        return Arrays.asList(
                Edge.of(nodes.get(0), nodes.get(1), 5l),
                Edge.of(nodes.get(1), nodes.get(2), 4l),
                Edge.of(nodes.get(2), nodes.get(3), 8l),
                Edge.of(nodes.get(3), nodes.get(2), 8l),
                Edge.of(nodes.get(3), nodes.get(4), 6l),
                Edge.of(nodes.get(0), nodes.get(3), 5l),
                Edge.of(nodes.get(2), nodes.get(4), 2l),
                Edge.of(nodes.get(4), nodes.get(1), 3l),
                Edge.of(nodes.get(0), nodes.get(4), 7l));
    }
    
    public static List<Edge> createOtherRoutes(List<Node> nodes) {
        return Arrays.asList(
                Edge.of(nodes.get(0), nodes.get(1), 6l),
                Edge.of(nodes.get(0), nodes.get(4), 4l),
                Edge.of(nodes.get(1), nodes.get(0), 6l),
                Edge.of(nodes.get(1), nodes.get(2), 2l),
                Edge.of(nodes.get(1), nodes.get(3), 4l),
                Edge.of(nodes.get(2), nodes.get(1), 3l),
                Edge.of(nodes.get(2), nodes.get(3), 1l),
                Edge.of(nodes.get(2), nodes.get(4), 7l),
                Edge.of(nodes.get(1), nodes.get(3), 8l),
                Edge.of(nodes.get(4), nodes.get(1), 5l),
                Edge.of(nodes.get(4), nodes.get(3), 7l));
    }
    
    public static Graph createGraph(List<Edge> edges) {
        return Graph.of(edges);
    }
    
    public static Graph updateGraph(Graph graph, List<Edge> edges) {
        graph.addRoutes(edges);
        return graph;
    }

}
