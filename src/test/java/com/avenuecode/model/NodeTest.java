package com.avenuecode.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NodeTest {

    @Test
    public void shouldReturnEquals() {
        Node nodeA = Node.of("A");
        Node otherNodeA = Node.of("A");

        assertThat(nodeA.equals(otherNodeA)).isTrue();
    }
    
    @Test
    public void shouldReturnHashEquals() {
        Node nodeA = Node.of("A");
        Node otherNodeA = Node.of("A");

        assertThat(nodeA.hashCode() == otherNodeA.hashCode()).isTrue();
    }

    @Test
    public void shouldReturnNotEquals() {
        Node nodeA = Node.of("A");
        Node nodeB = Node.of("B");

        assertThat(nodeA.equals(nodeB)).isFalse();
    }
    
    @Test
    public void shouldReturnHashNotEquals() {
        Node nodeA = Node.of("A");
        Node nodeB = Node.of("B");

        assertThat(nodeA.hashCode() == nodeB.hashCode()).isFalse();
    }    


}