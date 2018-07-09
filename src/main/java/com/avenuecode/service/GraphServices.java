package com.avenuecode.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avenuecode.dto.GraphProjection;
import com.avenuecode.exceptions.GraphNotFoundException;
import com.avenuecode.model.Edge;
import com.avenuecode.model.Graph;
import com.avenuecode.model.Node;
import com.avenuecode.repository.GraphCommandRepository;
import com.avenuecode.repository.GraphQueryRepository;
import com.avenuecode.repository.NodeCommandRepository;
import com.avenuecode.repository.NodeQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GraphServices {

    @Autowired
    private GraphQueryRepository graphQueryRepo;

    @Autowired
    private GraphCommandRepository graphCommandRepo;

    @Autowired
    private NodeQueryRepository nodeQueryRepo;

    @Autowired
    private NodeCommandRepository nodeCommandRepo;

    @Autowired
    private ObjectMapper mapper;

    public Graph getGraph(Long graphId) {
        return graphQueryRepo.getOne(graphId);
    }

    public GraphProjection findGraph(Long graphId) {
        return graphQueryRepo.findById(graphId, GraphProjection.class)
        					.orElseThrow(() -> new GraphNotFoundException(graphId.toString()));
    }

    @Transactional
    public Graph processGraph(String json) throws IOException {
        List<Edge> edges =
                mapper.readValue(
                        mapper.readTree(json).get("data").toString(),
                        mapper.getTypeFactory().constructCollectionType(ArrayList.class, Edge.class));

        Graph newGraph =
                Graph.of(edges.stream()
                        .map(this::processRoute)
                        .collect(Collectors.toList()));

        return graphCommandRepo.save(newGraph);
    }

    private Edge processRoute(Edge edge) {
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
