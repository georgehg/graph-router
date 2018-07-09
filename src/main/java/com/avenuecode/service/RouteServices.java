package com.avenuecode.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenuecode.model.Node;
import com.avenuecode.model.Router;
import com.avenuecode.model.Router.DetailedRoute;
import com.avenuecode.repository.NodeQueryRepository;

@Service
public class RouteServices {
	
	@Autowired
    private NodeQueryRepository nodeQueryRepo;

    public Set<Router.SimpleRoute> findRoutes(Long graphId, String source, String target, Integer maxStops) {
    	Node sourceNode = nodeQueryRepo.findByName(source);
        Node targetNode = nodeQueryRepo.findByName(target);
        
    	Router router = Router.from(graphId, sourceNode);
        
        if (maxStops == null) {
        	router.initializeRoutes(targetNode);
        } else {
        	router.initializeRoutes(targetNode, maxStops);
        }
        
        return router.getSimpleRoutes();
    }
    
    public Long calculateDistance(Long graphId, List<String> path) {
        Node sourceNode = nodeQueryRepo.findByName(path.get(0));
        Node targetNode = nodeQueryRepo.findByName(path.get(path.size()-1));

        Router router = Router.from(graphId, sourceNode);
        router.initializeRoutes(targetNode);
        
        return router.getPathDistance(path.stream().collect(Collectors.joining()));
    }
    
    public DetailedRoute calculateShortestDistance(Long graphId, String source, String target) {
    	Node sourceNode = nodeQueryRepo.findByName(source);
        Node targetNode = nodeQueryRepo.findByName(target);
        
    	Router router = Router.from(graphId, sourceNode);
        router.initializeRoutes(targetNode);
        
    	return router.getShortestPathDistance();
    }

}
