package com.avenuecode;

import com.avenuecode.model.Edge;
import com.avenuecode.model.Graph;
import com.avenuecode.repository.GraphCommandRepository;
import com.avenuecode.repository.GraphQueryRepository;
import com.avenuecode.repository.NodeCommandRepository;
import com.avenuecode.repository.NodeQueryRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class AbstractRepositoryTests {

    @Autowired
    protected NodeCommandRepository nodeCommandRepo;

    @Autowired
    protected NodeQueryRepository nodeQueryRepo;

    @Autowired
    protected GraphCommandRepository graphCommandRepo;

    @Autowired
    protected GraphQueryRepository graphQueryRepo;

    @Before
    public void cleanDataBase() {
        graphCommandRepo.deleteAll();
        nodeCommandRepo.deleteAll();
    }

    protected Graph saveGraph(List<Edge> edges) {
        Graph newGraph = Graph.of(edges);
        return graphCommandRepo.save(newGraph);
    }

}
