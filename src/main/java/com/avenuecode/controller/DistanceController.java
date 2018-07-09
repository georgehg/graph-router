package com.avenuecode.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.model.Graph;
import com.avenuecode.model.Router.DetailedRoute;
import com.avenuecode.service.GraphServices;
import com.avenuecode.service.RouteServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/distance")
public class DistanceController {

    @Autowired
    private GraphServices graphServices;

    @Autowired
    private RouteServices routerServices;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnDistance(@RequestBody(required = true) String data) throws Exception {
        Graph savedGraph = graphServices.processGraph(data);
        List<String> path = graphServices.processPath(data);

        if (path.size() <= 1 ) {
            return ResponseEntity.ok(createDistanceObject(0l));
        } else {
            return ResponseEntity.ok(createDistanceObject(routerServices.calculateDistance(savedGraph.getId(), path)));
        }
    }

    @PostMapping(value = "/{graphId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnDistanceFromGraph(@PathVariable Long graphId,
                                                          @RequestBody(required = true) String data) throws Exception {
        List<String> path = graphServices.processPath(data);

        if (path.size() <= 1 ) {
            return ResponseEntity.ok(createDistanceObject(0l));
        } else {
        	return ResponseEntity.ok(
        		createDistanceObject(routerServices.calculateDistance(graphServices.findGraph(graphId).getId(), path)));
        }

    }
    
    @PostMapping(value = "/from/{town1}/to/{town2}",
    				consumes = MediaType.APPLICATION_JSON_VALUE,
    				produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> returnShortestDistance(@PathVariable String town1,
            											 @PathVariable String town2,
            											 @RequestBody(required = true) String data) throws Exception {
        Graph savedGraph = graphServices.processGraph(data);
        
        return ResponseEntity.ok(
        		createDistanceObject(routerServices.calculateShortestDistance(savedGraph.getId(), town1, town2)));
    }
    
    @PostMapping(value = "/{graphId}/from/{town1}/to/{town2}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> returnShortestDistanceFromGraph(@PathVariable Long graphId,
																  @PathVariable String town1,
	    											 			  @PathVariable String town2) throws Exception {	
    	return ResponseEntity.ok(
    			createDistanceObject(routerServices.calculateShortestDistance(
    					graphServices.findGraph(graphId).getId(), town1, town2)));
}

    private Object createDistanceObject(Long distance) {
        ObjectNode node = mapper.createObjectNode();
        node.put("distance", distance);
        return node;
    }
    
    private Object createDistanceObject(DetailedRoute route) {
        ArrayNode path = mapper.createArrayNode();  
        Arrays.asList(route.getRoute().split("")).forEach(path::add);
        
        ObjectNode node = mapper.createObjectNode();
        node.put("distance", route.getDistance());
        node.putArray("path").addAll(path);
        return node;
    }
}
