package br.radixeng.model;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface GraphDataProjection {

    Long getId();

    @Value("#{target.edges}")
    List<RouteProjection> getData();

}