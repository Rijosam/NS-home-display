package com.home.traininfo.external.ns;

import java.util.List;

public record DeparturesPayload(Payload payload) {
    public record Payload(String source, List<Departure> departures){}
}
