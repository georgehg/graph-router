package com.avenuecode.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.dto.GraphProjection;
import com.avenuecode.model.Graph;
import com.avenuecode.service.GraphServices;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphServices graphServices;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphProjection> saveGraph(@RequestBody(required = true) String data) throws Exception {
        Graph savedGraph = graphServices.processGraph(data);
        return ResponseEntity.created(URI.create("localhost:8080/graph/" + String.valueOf(savedGraph.getId())))
                            .body(graphServices.findGraph(savedGraph.getId()));
    }

    @GetMapping(value = "/{graphId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphProjection> returnGraph(@PathVariable Long graphId) {
        return ResponseEntity.ok(graphServices.findGraph(graphId));
    }

}
