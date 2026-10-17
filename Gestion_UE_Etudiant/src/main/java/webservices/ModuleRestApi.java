package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/modules")
public class ModuleRestApi {
    static ModuleBusiness helper = new ModuleBusiness();

    // 1. Création d'un nouveau Module
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModule(Module module) {
        if (helper.addModule(module)) {
            return Response.status(201).entity(helper.getAllModules()).build();
        }
        return Response.status(404).entity("Erreur lors de l'ajout").build();
    }

    // 2. Récupération de la liste de tous les modules
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModules() {
        return Response.status(200).entity(helper.getAllModules()).build();
    }

    // 3. Récupération d'un module par sa matricule
    @GET
    @Path("/{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModuleByMatricule(@PathParam("matricule") String matricule) {
        Module m = helper.getModuleByMatricule(matricule);
        if (m != null) {
            return Response.status(200).entity(m).build();
        }
        return Response.status(404).entity("Module non trouvé").build();
    }

    // 4. Suppression d'un module par matricule
    @DELETE
    @Path("/{matricule}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteModule(@PathParam("matricule") String matricule) {
        if (helper.deleteModule(matricule)) {
            return Response.status(200).entity("Suppression réussie").build();
        }
        return Response.status(404).entity("Module non trouvé").build();
    }

    // 5. Modification d'un module par matricule
    @PUT
    @Path("/{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateModule(@PathParam("matricule") String matricule, Module module) {
        if (helper.updateModule(matricule, module)) {
            return Response.status(200).entity("Modification réussie").build();
        }
        return Response.status(404).entity("Module non trouvé").build();
    }

    // 6. Récupérer tous les modules associés à une UE spécifique (codeUE)
    @GET
    @Path("/UE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModulesByUE(@QueryParam("codeUE") int codeUE) {
        UniteEnseignement ue = new UniteEnseignement();
        ue.setCode(codeUE);
        List<Module> modules = helper.getModulesByUE(ue);
        if (modules != null && !modules.isEmpty()) {
            return Response.status(200).entity(modules).build();
        }
        return Response.status(404).entity("Aucun module trouvé pour cette UE").build();
    }
}