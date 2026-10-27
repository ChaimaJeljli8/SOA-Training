package graphql;

// Import the business logic class responsible for managing "UniteEnseignement" data
import business.ModuleBusiness;
import business.UniteEnseignementBusiness;
import entities.Module;

// Import the GraphQL library class used to define the root of GraphQL queries
import com.coxautodev.graphql.tools.GraphQLRootResolver;

// Import the entity class that represents a Teaching Unit (Unité d’Enseignement)
import entities.UniteEnseignement;

import java.util.List;

/**
 * This class acts as the root resolver for GraphQL queries.
 * It tells GraphQL how to fetch data when a query is executed.
 *
 * In this example, it handles queries related to "UniteEnseignement".
 */
public class QueryResolver implements GraphQLRootResolver {

    // Attribute used to call the business logic layer (service) for data operations
    public UniteEnseignementBusiness helper;
    public ModuleBusiness moduleHelper;
    /**
     * Constructor of the resolver.
     * It initializes the business helper that provides methods to retrieve UE data.
     */
    public QueryResolver(){
        helper = new UniteEnseignementBusiness();
        moduleHelper = new ModuleBusiness();
    }

    /**
     * GraphQL Query: allUEs
     *
     * This method will be automatically exposed to the GraphQL schema as a query named "allUEs".
     *
     * When a client sends the GraphQL query:
     *
     *    query {
     *        allUEs {
     *            code
     *            domaine
     *        }
     *    }
     *
     * GraphQL will call this method to return a list of "UniteEnseignement" objects.
     *
     * @return List of all Teaching Units (Unité d’Enseignement)
     */
    public List<UniteEnseignement> allUEs(){
        // Delegates the request to the business layer method getListeUE()
        // which fetches all UE entities (e.g., from a database or a static list)
        return helper.getListeUE();
    }

    // ---------- Partie A : Modules ----------

    /** A.1 - Liste de tous les modules */
    public List<Module> allModules() {
        return moduleHelper.getAllModules();
    }

    /** A.4 - Modules d'un type donné (TRANSVERSAL, PROFESSIONNEL, RECHERCHE) */
    public List<Module> modulesByType(Module.TypeModule type) {
        return moduleHelper.getModulesByType(type);
    }

    /** A.5 - Module ayant une matricule donnée (null si introuvable) */
    public Module moduleByMatricule(String matricule) {
        return moduleHelper.getModuleByMatricule(matricule);
    }


    // ---------- Partie B : Unités d'Enseignement ----------

    /** B.2 - Unité d'Enseignement à partir de son code unique (null si introuvable) */
    public UniteEnseignement ueByCode(int code) {
        return helper.getUEByCode(code);
    }

    /** B.3 - Toutes les UE d'un domaine donné */
    public List<UniteEnseignement> uesByDomaine(String domaine) {
        return helper.getUEByDomaine(domaine);
    }

    /** B.4 - Toutes les UE d'un semestre donné */
    public List<UniteEnseignement> uesBySemestre(int semestre) {
        return helper.getUEBySemestre(semestre);
    }

}
