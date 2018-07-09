package com.avenuecode.dto;

import java.util.Set;

import com.avenuecode.model.Router;

import lombok.Value;

@Value(staticConstructor = "of")
public class RouteDto {

    private Set<Router.SimpleRoute> routes;

}
