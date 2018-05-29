package br.radixeng.model;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@ToString
@Embeddable
public class Edge {

    @NotNull
    @ManyToOne(cascade = {CascadeType.ALL})
    private final Node source;

    @NotNull
    @ManyToOne(cascade = {CascadeType.ALL})
    private final Node target;

    @NotNull
    private final Long distance;

    private Edge(Node source, Node target, Long distance) {
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public Edge() {
        this(null, null, null);
    }

    public static Edge of(Node source, Node target, Long distance) {
        return new Edge(source, target, distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if( (obj == null) || !(obj instanceof Edge) ) {
            return false;
        }

        Edge other = (Edge) obj;

        if (!Objects.equals(source, other.source)) return false;
        if (!Objects.equals(target, other.target)) return false;
        return Objects.equals(distance, other.distance);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + distance.hashCode();
        return result;
    }
}
