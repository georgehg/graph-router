package br.radixeng.controller;

import br.radixeng.model.*;
import br.radixeng.service.GraphServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    GraphServices graphServices;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDataProjection> returnGraph(@RequestBody(required = true) String data) throws Exception {
        Graph savedGraph = graphServices.processGraph(data);
        return ResponseEntity.created(URI.create("localhost:8080/graph/" + String.valueOf(savedGraph.getId())))
                            .body(graphServices.getGraphPojection(savedGraph.getId()));

    }

    @GetMapping(value = "/{graphId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDataProjection> returnGraph(@PathVariable Long graphId) {

        if (!graphServices.existsGraph(graphId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(graphServices.getGraphPojection(graphId));
    }


}
