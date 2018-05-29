package br.radixeng.controller;

import br.radixeng.model.Graph;
import br.radixeng.model.RouteDto;
import br.radixeng.service.GraphServices;
import br.radixeng.service.RouteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/routes")
public class RoutesController {

    @Autowired
    GraphServices graphServices;

    @Autowired
    RouteServices routeServices;

    @PostMapping(value = "/from/{town1}/to/{town2}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteDto> availableRoutes(
                                    @PathVariable String town1,
                                    @PathVariable String town2,
                                    @RequestParam(value = "maxStops", required=false) Integer maxStops,
                                    @RequestBody(required = true) String data) throws Exception {

        Graph savedGraph = graphServices.processGraph(data);
        return ResponseEntity.ok(RouteDto.of(routeServices.findRoutes(savedGraph.getId(), town1, town2, maxStops)));

    }

    @PostMapping(value = "/{graphId}/from/{town1}/to/{town2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteDto> availableRoutes(
                                    @PathVariable Long graphId,
                                    @PathVariable String town1,
                                    @PathVariable String town2,
                                    @RequestParam(value = "maxStops", required=false) Integer maxStops) {

       if (!graphServices.existsGraph(graphId)) {
           return ResponseEntity.notFound().build();
       }

        return ResponseEntity.ok(RouteDto.of(routeServices.findRoutes(graphId, town1, town2, maxStops)));

    }



}
