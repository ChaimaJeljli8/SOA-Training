package graphql;

// Import the business logic class responsible for managing "UniteEnseignement" data
import business.ModuleBusiness;
import business.UniteEnseignementBusiness;
import entities.Module;

// Import the GraphQL library class used to define the root of GraphQL mutations
import com.coxautodev.graphql.tools.GraphQLRootResolver;

// Import the entity class representing a Teaching Unit (Unité d’Enseignement)
import entities.UniteEnseignement;

/**
 * This class defines all GraphQL mutations related to the "UniteEnseignement" entity.
 * A mutation in GraphQL is used to perform write operations (create, update, delete)
 * as opposed to queries, which are for read operations.
 */
public class MutationResolver implements GraphQLRootResolver {

    // Attribute used to access the business logic (service layer)
    public UniteEnseignementBusiness helper;
    public ModuleBusiness moduleHelper;
    /**
     * Constructor of the resolver.
     * It initializes the business helper that manages the teaching units.
     */
    public MutationResolver() {

        helper = new UniteEnseignementBusiness();
        moduleHelper = new ModuleBusiness();
    }

    /**
     * GraphQL Mutation: addUniteEnseignement
     *
     * This method allows clients to add a new "Unité d’Enseignement" to the system.
     *
     * When a GraphQL mutation like the following is executed:
     *
     * mutation {
     *     addUniteEnseignement(
     *         code: 101,
     *         domaine: "Informatique",
     *         responsable: "Dr. Smith",
     *         credits: 4,
     *         semestre: 2
     *     )
     * }
     *
     * GraphQL calls this Java method to create and save a new teaching unit.
     */
    public boolean addUniteEnseignement(int code, String domaine, String responsable, int credits, int semestre) {
        // Create a new UniteEnseignement object with the provided parameters
        UniteEnseignement ue = new UniteEnseignement(code, domaine, responsable, credits, semestre);

        // Call the business logic method to add the new unit
        // The helper handles the actual persistence or logic of adding the entity
        return helper.addUniteEnseignement(ue);
    }


    // ---------- Partie A : Modules ----------

    /** A.2 - Création d'un module (retourne true si l'UE existe et que le module est ajouté) */
    public boolean addModule(String matricule, String libelle, int coef, int volumeHoraire,
                             int codeUE, Module.TypeModule type) {
        UniteEnseignement ue = helper.getUEByCode(codeUE);
        if (ue == null) {
            return false; // UE inexistante : on refuse la création
        }
        Module module = new Module(matricule, libelle, coef, volumeHoraire, type, ue);
        return moduleHelper.addModule(module);
    }

    /** A.3 - Modification d'un module identifié par sa matricule (retourne le module modifié, ou null) */
    public Module updateModule(String matricule, String libelle, int coef, int volumeHoraire,
                               int codeUE, Module.TypeModule type) {
        UniteEnseignement ue = helper.getUEByCode(codeUE);
        if (ue == null) {
            return null;
        }
        Module updated = new Module(matricule, libelle, coef, volumeHoraire, type, ue);
        if (moduleHelper.updateModule(matricule, updated)) {
            return moduleHelper.getModuleByMatricule(matricule);
        }
        return null;
    }

    /** A.6 - Suppression d'un module identifié par sa matricule */
    public String deleteModule(String matricule) {
        return moduleHelper.deleteModule(matricule)
                ? "deleted successfully"
                : "module not found";
    }


}
