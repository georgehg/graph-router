package com.avenuecode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.dto.RouteDto;
import com.avenuecode.model.Graph;
import com.avenuecode.service.GraphServices;
import com.avenuecode.service.RouteServices;

@RestController
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private GraphServices graphServices;

    @Autowired
    private RouteServices routeServices;

    @PostMapping(value = "/from/{town1}/to/{town2}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteDto> availableRoutes(
                                    @PathVariable String town1,
                                    @PathVariable String town2,
                                    @RequestParam(value = "maxStops", required=false) Integer maxStops,
                                    @RequestBody(required = true) String data) throws Exception {

        Graph savedGraph = graphServices.processGraph(data);
        return ResponseEntity.ok(RouteDto.of(
        		routeServices.findRoutes(savedGraph.getId(), town1, town2, maxStops)));
    }

    @PostMapping(value = "/{graphId}/from/{town1}/to/{town2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteDto> availableRoutes(
                                    @PathVariable Long graphId,
                                    @PathVariable String town1,
                                    @PathVariable String town2,
                                    @RequestParam(value = "maxStops", required=false) Integer maxStops) {

        return ResponseEntity.ok(RouteDto.of(
        		routeServices.findRoutes(graphServices.findGraph(graphId).getId(), town1, town2, maxStops)));
    }

}
