package br.radixeng.controller;

import br.radixeng.model.Graph;
import br.radixeng.service.GraphServices;
import br.radixeng.service.RouteServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distance")
public class DistanceController {

    @Autowired
    GraphServices graphServices;

    @Autowired
    RouteServices routeServices;

    @Autowired
    ObjectMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnDistance(@RequestBody(required = true) String data) throws Exception {
        Graph savedGraph = graphServices.processGraph(data);
        List<String> path = graphServices.processPath(data);

        if (path.size() <= 1 ) {
            return ResponseEntity.ok(createDistanceObject(0l));
        } else {
            return ResponseEntity.ok(createDistanceObject(routeServices.calculateDistance(savedGraph.getId(), path)));
        }

    }

    @PostMapping(value = "/{graphId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnDistanceFromGraph(@PathVariable Long graphId,
                                                          @RequestBody(required = true) String data) throws Exception {
        if (!graphServices.existsGraph(graphId)) {
            return ResponseEntity.notFound().build();
        }

        Graph savedGraph = graphServices.getGraph(graphId);

        List<String> path = graphServices.processPath(data);

        if (path.size() <= 1 ) {
            return ResponseEntity.ok(createDistanceObject(0l));
        } else {
            return ResponseEntity.ok(createDistanceObject(routeServices.calculateDistance(savedGraph.getId(), path)));
        }

    }

    private Object createDistanceObject(Long distance) {
        ObjectNode node = mapper.createObjectNode();
        node.put("distance", distance);
        return node;
    }
}
