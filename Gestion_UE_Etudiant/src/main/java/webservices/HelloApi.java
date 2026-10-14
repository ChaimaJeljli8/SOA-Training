package webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

//jax-rs : type de code java pour les services
@Path("/hello1")
public class HelloApi {
    @GET //methode
    @Path("/hi") //URI
    @Produces(MediaType.TEXT_PLAIN) //representetion
    public Response sayHello1() {

        return Response
                .status(200)
                .entity("Hello World!")
                .build();
    }
}
