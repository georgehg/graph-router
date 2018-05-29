package br.radixeng.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@ToString
public class Router {

    private Node source;

    private Long distance;

    private List<Router> adjacentRouters = new LinkedList<>();

    private List<DetailedRoute> routes = new ArrayList<>();

    private Router(Node source, Long distance) {
        this.source = source;
        this.distance = distance;
    }

    public static Router of(Node source) {
        return new Router(source, 0l);
    }

    public static Router of(Node source, Long distance) {
        return new Router(source, distance);
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class SimpleRoute {
        private final String route;
        private final Integer stops;

        public SimpleRoute(String route, Integer stops) {
            this.route = route;
            this.stops = stops;
        }
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class DetailedRoute extends SimpleRoute {
        private final Long distance;

        public DetailedRoute(String route, Integer stops, Long distance) {
            super(route, stops);
            this.distance = distance;
        }
    }

    public Node getSource() {
        return this.source;
    }

    public List<Router> getAdjacentRouters() {
        return this.adjacentRouters;
    }

    public Long getDistance() {
        return this.distance;
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
                    .orElse(-1l);
    }

    public void loadRoutes(List<Router> routers) {
        adjacentRouters.addAll(routers);
    }

    public void calculatePossibleRoutes() {
        addPossibleRoute(this, this.source.getName(), 0, this.distance);
    }

    private void addPossibleRoute(Router actual, String route, Integer stops, Long distance) {
        if (actual.adjacentRouters.isEmpty()) {
            routes.add(new DetailedRoute(route, stops, distance + actual.getDistance()));
            return;
        }

        actual.adjacentRouters.forEach(adjacent -> {
            String newRoute = route + adjacent.source.getName();
            addPossibleRoute(adjacent, newRoute, stops + 1, distance + actual.getDistance());
        });

    }

}
