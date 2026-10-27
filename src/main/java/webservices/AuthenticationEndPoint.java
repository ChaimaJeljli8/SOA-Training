package webservices;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/*
 *
 * Si vous avez besoin d’obtenir des informations sur le contexte d’exécution de la requête,
 * vous pouvez utilisez l’annotation @javax.ws.rs.core.Context pour obtenir une instance d’une classe particulière.
 * UriInfo : Cette interface donne accès à l’URI de la requête.
 *
 */


@Path("/authentification")
public class AuthenticationEndPoint {

    // ======================================
    // = Injection Points =
    // ======================================

    @Context
    private UriInfo uriInfo;

    @POST
    //@Produces(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    // @Produces(MediaType.TEXT_PLAIN)
    // @Consumes(MediaType.APPLICATION_JSON)

    //// this is a first alternative (with formParam)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {
        //// this is a second alternative (with the Credentials class)
        // public Response authenticateUser(Credentials cred) {
        try {

            // Authenticate the user using the credentials provided
            authenticate(username, password);
            // authenticate(cred.getUsername(), cred.getPassword());

            // Issue a token for the user //2
            //String token = issueToken(username);
            // String token = issueToken(cred.getUsername());

            // Return the token on the response
            return Response.ok(tokensJson(username)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }


    //  exchange a valid refresh token for a new access token (+ a new refresh token)
    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response refresh(@FormParam("refreshToken") String refreshToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(getKey())
                    .parseClaimsJws(refreshToken).getBody();   // checks signature + expiration
            if (!"refresh".equals(claims.get("type"))) {
                throw new Exception("Not a refresh token");
            }
            return Response.ok(tokensJson(claims.getSubject())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void authenticate(String username, String password) {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        System.out.println("Authenticating user...");

    }

    private Key getKey() {
        String keyString = "simplekey";
        return new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
    }
/*
    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token

        String keyString = "simplekey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0,
                keyString.getBytes().length, "DES");
        System.out.println("the key is : " + key.hashCode());

        System.out.println("uriInfo.getAbsolutePath().toString() : " + uriInfo.getAbsolutePath().toString());
        System.out.println("Expiration date: " + toDate(LocalDateTime.now().plusMinutes(15L)));


        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        System.out.println("the returned token is : " + jwtToken);

        return jwtToken;
    }
*/
    private String issueToken(String username, long minutes, String type) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .claim("type", type)                       // "access" or "refresh"
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(minutes)))
                .signWith(SignatureAlgorithm.HS512, getKey())
                .compact();
    }
    private String tokensJson(String username) {
        String access  = issueToken(username, 15L, "access");            // 15 min
        String refresh = issueToken(username, 7L * 24 * 60, "refresh");  // 7 days
        return "{\"accessToken\":\"" + access + "\",\"refreshToken\":\"" + refresh + "\"}";
    }
    // ======================================
    // = Private methods =
    // ======================================

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}

