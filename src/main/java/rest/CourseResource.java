
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.CourseDTO;
import errorhandling.NotFoundException;
import facades.CourseFacade;
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

@Path("course")
public class CourseResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final CourseFacade FACADE = CourseFacade.getCourseFacade(EMF);

    @GET
    @Path("get")
    @RolesAllowed({"instructor", "student"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCourses() throws NotFoundException {
        return GSON.toJson(FACADE.getAllCourses());
    }

    @POST
    @Path("create")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCourse(String jsonString){
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        String courseName = json.get("courseName").getAsString();
        String courseDescription = json.get("courseDescription").getAsString();
        return GSON.toJson(FACADE.createCourse(courseName, courseDescription));
    }
    
    
    @PUT
    @Path("edit")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editCourse(String jsonString) throws NotFoundException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    int courseId = json.get("courseId").getAsInt();
    String newName = json.get("newName").getAsString();
    String newDescription = json.get("newDescription").getAsString();
    return GSON.toJson(FACADE.editCourse(courseId, newName, newDescription));
    }
    
    @DELETE
    @Path("delete/{courseId}")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCourse(@PathParam("courseId") int courseId) throws NotFoundException {
        return GSON.toJson(FACADE.deleteCourse(courseId));
    }
    
}
