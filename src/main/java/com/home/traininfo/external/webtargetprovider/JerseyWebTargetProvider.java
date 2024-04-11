package com.home.traininfo.external.webtargetprovider;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Component
public class JerseyWebTargetProvider implements WebTargetProvider {

    @Override
    public WebTarget getWebTarget(URI uri) {
        return ClientBuilder.newBuilder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .build()
                .target(uri);
    }
}
