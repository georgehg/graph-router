package com.avenuecode.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public interface GraphProjection {

    Long getId();

    @Value("#{target.getRoutes()}")
    List<RouteProjection> getData();

}