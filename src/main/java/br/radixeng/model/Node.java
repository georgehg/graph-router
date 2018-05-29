package br.radixeng.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="node", uniqueConstraints={
        @UniqueConstraint(columnNames={"name"})
})
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private final String name;

    private Node(String name) {
        this.name = name;
    }

    public Node() {
        this(null);
    }

    public static Node of(String name) {
        return new Node(name);
    }
}