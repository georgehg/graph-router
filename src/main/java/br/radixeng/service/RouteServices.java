package br.radixeng.service;

import br.radixeng.model.Node;
import br.radixeng.model.Router;
import br.radixeng.repository.GraphQueryRepository;
import br.radixeng.repository.NodeQueryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RouteServices {

    @Autowired
    GraphQueryRepository graphQueryRepo;

    @Autowired
    NodeQueryRepository nodeQueryRepo;

    @Autowired
    ObjectMapper mapper;

    public Set<Router.SimpleRoute> findRoutes(Long graphId, String source, String target, Integer maxStops) {

        Node sourceNode = nodeQueryRepo.findByName(source);
        Node targetNode = nodeQueryRepo.findByName(target);

        Router router = Router.of(sourceNode);

        if (maxStops == null) {
            if (graphId == null) {
                maxStops = graphQueryRepo.findAll()
                                        .stream()
                                        .map(graph -> graph.getEdges().size())
                                        .reduce(0, Integer::sum);
            } else {
                maxStops = graphQueryRepo.getOne(graphId).getEdges().size();
            }
        }

        loadRoutes(graphId, router, sourceNode.getId(), targetNode, maxStops);
        router.calculatePossibleRoutes();

        return router.getSimpleRoutes();
    }

    public Long calculateDistance(Long graphId, List<String> path) {
        Node sourceNode = nodeQueryRepo.findByName(path.get(0));
        Node targetNode = nodeQueryRepo.findByName(path.get(path.size()-1));

        Integer maxStops;
        if (graphId == null) {
            maxStops = graphQueryRepo.findAll()
                    .stream()
                    .map(graph -> graph.getEdges().size())
                    .reduce(0, Integer::sum);
        } else {
            maxStops = graphQueryRepo.getOne(graphId).getEdges().size();
        }

        Router router = Router.of(sourceNode);
        loadRoutes(graphId, router, sourceNode.getId(), targetNode, maxStops);
        router.calculatePossibleRoutes();

        return router.getPathDistance(path.stream().collect(Collectors.joining()));
    }

    public void loadRoutes(Long graphId, Router sourceRouter, Long startId, Node targetNode, Integer stops){
        sourceRouter.loadRoutes(
                findAllAdjacents(graphId, sourceRouter, targetNode)
                        .stream()
                        .map(edge -> Router.of(
                                nodeQueryRepo.getOne(Long.valueOf(edge[0].toString())),
                                Long.valueOf(edge[1].toString())))
                        .collect(Collectors.toList()));

        if (stops != null && stops == 1) return;

        sourceRouter.loadRoutes(
                findDirectNodes(graphId, sourceRouter, startId)
                        .stream()
                        .map(edge -> {
                            Router router = Router.of(
                                    nodeQueryRepo.getOne(Long.valueOf(edge[0].toString())),
                                    Long.valueOf(edge[1].toString()));
                            loadRoutes(graphId, router, startId, targetNode, stops == null ? null : stops-1);
                            return router;
                        })
                        .filter(router -> !router.getAdjacentRouters().isEmpty())
                        .collect(Collectors.toList()));
    }

    private Set<Object[]> findAllAdjacents(Long graphId, Router sourceRouter, Node targetNode) {
        if (graphId == null) {
            return nodeQueryRepo.findAdjacentNodes(sourceRouter.getSource().getId(), targetNode.getId());
        } else {
            return nodeQueryRepo.findAdjacentNodes(graphId, sourceRouter.getSource().getId(), targetNode.getId());
        }
    }

    private Set<Object[]> findDirectNodes(Long graphId, Router sourceRouter, Long startId) {
        if (graphId == null) {
            return nodeQueryRepo.findDirectNodes(sourceRouter.getSource().getId(), startId);
        } else {
            return nodeQueryRepo.findDirectNodes(graphId, sourceRouter.getSource().getId(), startId);

        }
    }

}
