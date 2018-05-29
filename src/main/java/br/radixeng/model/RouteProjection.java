package br.radixeng.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

public interface RouteProjection {

    @Value("#{@routeMapping.getSource(target)}")
    String getSource();

    @JsonProperty("target")
    @Value("#{@routeMapping.getDestination(target)}")
    String getDestination();

    @Value("#{@routeMapping.getDistance(target)}")
    Long getDistance();
}
