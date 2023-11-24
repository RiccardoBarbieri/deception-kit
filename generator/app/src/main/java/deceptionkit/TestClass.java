package deceptionkit;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;

public class TestClass {

    public static void main(String[] args) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8100")
                .grantType(OAuth2Constants.PASSWORD)
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        for (UserRepresentation user : keycloak.realm("master").users().list()) {
            System.out.println(user.getUsername());
        }
    }
}
