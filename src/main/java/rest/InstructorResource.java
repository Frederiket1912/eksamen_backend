package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import errorhandling.NotFoundException;
import facades.CourseFacade;
import facades.InstructorFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

@Path("instructor")
public class InstructorResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final InstructorFacade FACADE = InstructorFacade.getInstructorFacade(EMF);
    
    @GET
    @Path("get")
    @RolesAllowed({"instructor", "student"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllInstructors() throws NotFoundException {
        return GSON.toJson(FACADE.getAllInstructors());
    }
    
    @POST
    @Path("create")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createInstructor(String jsonString){
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        String name = json.get("name").getAsString();
        String password = json.get("password").getAsString();
        return GSON.toJson(FACADE.createInstructor(name, password));
    }
    
    @PUT
    @Path("edit")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editInstructor(String jsonString) throws NotFoundException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    int instructorId = json.get("instructorId").getAsInt();
    String newName = json.get("newName").getAsString();
    String newPassword = json.get("newPassword").getAsString();
    return GSON.toJson(FACADE.editInstructor(instructorId, newName, newPassword));
    }
    
    @DELETE
    @Path("delete/{instructorId}")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteInstructor(@PathParam("instructorId") int instructorId) throws NotFoundException {
        return GSON.toJson(FACADE.deleteInstructor(instructorId));
    }
}
