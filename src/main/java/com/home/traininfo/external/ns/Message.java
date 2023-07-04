package com.home.traininfo.external.ns;

public record Message(
        String id,
        String externalId,
        String head,
        String text,
        String lead,
        Integer routeIdxFrom,
        Integer routeIdxTo) {
}

