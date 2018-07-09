package com.avenuecode.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.avenuecode.exceptions.InvalidRouteException;

import lombok.ToString;

@ToString
@Entity
@Table(name="graph")
public class Graph {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "route", joinColumns = @JoinColumn(name = "graph_id"))
    private final Set<Edge> routes = new HashSet<>();

    private Graph(List<Edge> routes) {
        this.addRoutes(routes);
    }

    protected Graph() {}

    public static Graph of(List<Edge> routes) {
        return new Graph(routes);
    }

    public Long getId() {
        return id;
    }

    public Set<Edge> getRoutes() {
        return Collections.unmodifiableSet(routes);
    }

    public void addRoutes(List<Edge> edges) {
        routes.clear();
        edges.forEach(this::addRoute);
    }
    
    private void addRoute(Edge newEdge) {    	
    	if (newEdge.getSource().equals(newEdge.getTarget())) {
    		throw new InvalidRouteException(newEdge.toString());
    	}

    	if (!routes.add(newEdge)) {
    		throw new InvalidRouteException(newEdge.toString());
    	}
    }

}
