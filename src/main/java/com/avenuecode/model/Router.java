package com.avenuecode.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
public class Router {
	
	private Long graphId;

    private Route source;
    
    private Node target;

    private List<DetailedRoute> routes = new ArrayList<>();

    private Router(Long graphId, Route source) {
        this.graphId = graphId;
        this.source = source;
    }

    public static Router from(Node source) {
        return new Router(null, Route.head(source));
    }

    public static Router from(Long graphId, Node source) {
        return new Router(graphId, Route.head(source));
    }
    
    public List<DetailedRoute> getRoutes() {
        return Collections.unmodifiableList(this.routes);
    }

    public Set<SimpleRoute> getSimpleRoutes() {
        return routes.stream()
                .filter(route -> route.getStops() > 0)
                .map(route -> new SimpleRoute(route.getRoute(), route.getStops()))
                .collect(Collectors.toSet());
    }

    public Long getPathDistance(String path) {
        return routes.stream()
                    .filter(route -> route.getRoute().equals(path))
                    .map(route -> route.getDistance())
                    .findFirst()
                    .orElse(-1L);
    }

    public DetailedRoute getShortestPathDistance() {
    	if (this.source.getHead().equals(this.target)) {
    		return DetailedRoute.zero(this.target.getName());
    	}
    	
    	if (routes.size() <= 1) {
    		return DetailedRoute.empty();
    	}
        
    	return routes.stream()
        			.min(Comparator.comparing(DetailedRoute::getDistance))
        			.get();
    }

    public void initializeRoutes(Node target) {
    	this.target = target;
        this.source.startRouting(this.graphId, target);
        this.routes.clear();
        addPossibleRoute(this.source, new StringBuilder().append(this.source.getHead().getName()), 0, this.source.getDistance());
    }

    public void initializeRoutes(Node target, Integer maxStops) {
    	this.target = target;
        this.source.startRouting(this.graphId, target, maxStops);
        this.routes.clear();
        addPossibleRoute(this.source, new StringBuilder().append(this.source.getHead().getName()), 0, this.source.getDistance());
    }

    private void addPossibleRoute(Route actual, StringBuilder route, Integer stops, Long distance) {
        if (actual.getAdjacentNodes().isEmpty()) {
            routes.add(new DetailedRoute(route.toString(), stops, distance + actual.getDistance()));
            return;
        }

        actual.getAdjacentNodes()
                .forEach(adjacent -> {
                            StringBuilder routePath = new StringBuilder().append(route).append(adjacent.getHead().getName());
                            addPossibleRoute(adjacent, routePath, stops + 1, distance + actual.getDistance());
                });
    }
    
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class SimpleRoute {

        private final String route;

        private final Integer stops;

        public SimpleRoute(String route, Integer stops) {
            this.route = route;
            this.stops = stops;
        }

        public String getRoute() {
            return route;
        }

        public Integer getStops() {
            return stops;
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static class DetailedRoute extends SimpleRoute {
        private final Long distance;

        public DetailedRoute(String route, Integer stops, Long distance) {
            super(route, stops);
            this.distance = distance;
        }
        
        public static DetailedRoute empty() {
        	return new DetailedRoute("", 0, -1L);
        }
        
        public static DetailedRoute zero(String node) {
        	return new DetailedRoute(node+node, 0, 0L);
        }

        public Long getDistance() {
            return distance;
        }
    }

}
