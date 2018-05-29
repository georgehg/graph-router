package br.radixeng.model;

import br.radixeng.repository.NodeCommandRepository;
import br.radixeng.repository.NodeQueryRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeTest extends AbstractRepositoryTests{

    @Autowired
    NodeCommandRepository nodeCommandRepo;

    @Autowired
    NodeQueryRepository nodeQueryRepo;

    @Test
    @Transactional
    public void shouldCreateAndPersistNodes() {
        Node nodeA = Node.of("A");
        Node nodeB = Node.of("B");

        nodeCommandRepo.save(nodeA);
        nodeCommandRepo.save(nodeB);

        assertThat(nodeQueryRepo.findByName("A")).isNotNull();
        assertThat(nodeQueryRepo.findByName("B")).isNotNull();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldFailBecauseRepatedNode() {
        Node nodeA = Node.of("A");
        Node otherNodeA = Node.of("A");

        nodeCommandRepo.save(nodeA);
        nodeCommandRepo.save(otherNodeA);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailBecauseNullValue() {
        Node nodeA = Node.of(null);
        nodeCommandRepo.save(nodeA);
    }

    @Test
    public void shouldReturnEqualsSameNode() {
        Node nodeA = Node.of("A");
        Node otherNodeA = Node.of("A");

        assertThat(nodeA.equals(otherNodeA)).isTrue();
    }

    @Test
    public void shouldReturnNotEqualsDifferentNode() {
        Node nodeA = Node.of("A");
        Node nodeB = Node.of("B");

        assertThat(nodeA.equals(nodeB)).isFalse();
    }


}