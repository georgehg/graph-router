package com.avenuecode.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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

    protected Node() {
        this(null);
    }

    public static Node of(String name) {
        return new Node(name);
    }
    
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if( (obj == null) || !(obj instanceof Node) ) {
            return false;
        }

        Node other = (Node) obj;

        return Objects.equals(name, other.name);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
}