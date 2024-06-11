package com.deceptionkit.spring.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class KeycloakClientConfiguration {

    private String HOST;

    private String PORT;

    @Value("${keycloak.host}")
    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    @Value("${keycloak.port}")
    public void setPORT(String PORT) {
        this.PORT = PORT;
    }


    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://" + HOST + ":" + PORT)
                .grantType(OAuth2Constants.PASSWORD)
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
//                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }


}
