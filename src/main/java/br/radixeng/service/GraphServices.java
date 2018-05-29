package br.radixeng.service;

import br.radixeng.model.Edge;
import br.radixeng.model.Graph;
import br.radixeng.model.GraphDataProjection;
import br.radixeng.model.Node;
import br.radixeng.repository.GraphCommandRepository;
import br.radixeng.repository.GraphQueryRepository;
import br.radixeng.repository.NodeCommandRepository;
import br.radixeng.repository.NodeQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphServices {

    @Autowired
    GraphQueryRepository graphQueryRepo;

    @Autowired
    GraphCommandRepository graphCommandRepo;

    @Autowired
    NodeQueryRepository nodeQueryRepo;

    @Autowired
    NodeCommandRepository nodeCommandRepo;

    @Autowired
    ObjectMapper mapper;

    public Boolean existsGraph(Long graphId) {
        return graphQueryRepo.exists(graphId);
    }

    public Graph getGraph(Long graphId) {
        return graphQueryRepo.getOne(graphId);
    }

    public GraphDataProjection getGraphPojection(Long graphId) {
        return graphQueryRepo.findById(graphId, GraphDataProjection.class);
    }

    @Transactional
    public Graph processGraph(String json) throws IOException {
        List<Edge> edges =
                mapper.readValue(
                        mapper.readTree(json).get("data").toString(),
                        mapper.getTypeFactory().constructCollectionType(ArrayList.class, Edge.class));

        Graph newGraph =
                Graph.of(edges.stream()
                        .map(this::setUniqueNodes)
                        .collect(Collectors.toList()));

        return graphCommandRepo.save(newGraph);
    }

    private Edge setUniqueNodes(Edge edge) {
        Node source = nodeQueryRepo.findByName(edge.getSource().getName());
        if (source == null) {
            source = nodeCommandRepo.save(edge.getSource());
        }

        Node target = nodeQueryRepo.findByName(edge.getTarget().getName());
        if (target == null) {
            target = nodeCommandRepo.save(edge.getTarget());
        }

        return Edge.of(source, target, edge.getDistance());
    }

    public List<String> processPath(String json) throws IOException {
        return mapper.readValue(
                    mapper.readTree(json).get("path").toString(),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class));
    }
}
