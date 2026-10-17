package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ue")
public class UniteEnsRestApi {
    static UniteEnseignementBusiness helper = new UniteEnseignementBusiness(); // statful status because there is no db

    //liste des UE
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListeUe(){
        return Response
                .status(200)
                .entity(helper.getListeUE())
                .build();

    }

    //ajouter une UE
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addUe(UniteEnseignement ue){
        if (helper.addUniteEnseignement(ue)){
            return Response.status(201).entity("Success").build();
        }
       else
           return Response.status(400).entity("Erreur").build();
    }

    // Modifier une UE
    @PUT
    @Path("/update/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateUe(@PathParam("code") int code, UniteEnseignement ue) {
        if (helper.updateUniteEnseignement(code, ue)) {
            return Response.status(200).entity("Modification réussie").build();
        }
        return Response.status(404).entity("Introuvable").build();
    }

    // Supprimer une UE
    @DELETE
    @Path("/delete/{code}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUe(@PathParam("code") int code) {
        if (helper.deleteUniteEnseignement(code)) {
            return Response.status(200).entity("Suppression réussie").build();
        }
        return Response.status(404).entity("Introuvable").build();
    }

    //  Rechercher par  code
    @GET
    @Path("/search/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUe(@PathParam("code") int code) {

        List<UniteEnseignement> liste = helper.getListeUE();

        for (UniteEnseignement ue : liste) {
            if (ue.getCode() == code) {
                return Response.ok(ue).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("UE introuvable").build();
    }



}
