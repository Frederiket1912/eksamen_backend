package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import errorhandling.NotFoundException;
import facades.CourseFacade;
import facades.StudentFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;


@Path("student")
public class StudentResource {
    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final StudentFacade FACADE = StudentFacade.getStudentFacade(EMF);
    
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createStudent(String jsonString){
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        String name = json.get("name").getAsString();
        String password = json.get("password").getAsString();
        String phone = json.get("phone").getAsString();
        String email = json.get("email").getAsString();
        return GSON.toJson(FACADE.createStudent(name, password, phone, email));
    }
    
    @POST
    @Path("signup")
    @RolesAllowed({"instructor", "student"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String signUp(String jsonString) throws NotFoundException{
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        int studentId = json.get("studentId").getAsInt();
        int yogaClassId = json.get("yogaClassId").getAsInt();
        boolean payed = json.get("payed").getAsBoolean();
        return GSON.toJson(FACADE.SignUpToClass(studentId, yogaClassId, payed));
    }
}
