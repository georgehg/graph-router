package br.radixeng.model;

import lombok.Value;

import java.util.Set;

@Value(staticConstructor = "of")
public class RouteDto {

    private Set<Router.SimpleRoute> routes;

}
