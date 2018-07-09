package com.avenuecode.dto;

import org.springframework.stereotype.Component;

import com.avenuecode.model.Edge;

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
