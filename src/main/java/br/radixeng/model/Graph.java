package br.radixeng.model;

import lombok.ToString;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Entity
@Table(name="graph")
public class Graph {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "edge", joinColumns = @JoinColumn(name = "graph_id"))
    private final Set<Edge> edges = new HashSet<>();

    private Graph(List<Edge> edges) {
        this.addRoutes(edges);
    }

    public Graph() {}

    public static Graph of(List<Edge> edges) {
        return new Graph(edges);
    }

    public Long getId() {
        return id;
    }

    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    public void addRoutes(List<Edge> newEdges) {
        this.edges.clear();
        newEdges.forEach(route -> {
            if (!this.edges.add(route)) {
                this.edges.remove(route);
                this.edges.add(route);
            }
        });
    }

}
