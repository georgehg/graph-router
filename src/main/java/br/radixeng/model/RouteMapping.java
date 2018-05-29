package br.radixeng.model;

import org.springframework.stereotype.Component;

@Component
public class RouteMapping {

    public String getSource(Edge edge) {
        return edge.getSource().getName();
    }

    public String getDestination(Edge edge) {
        return edge.getTarget().getName();
    }

    public Long getDistance(Edge edge) {
        return edge.getDistance();
    }
}
