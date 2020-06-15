package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.CourseDTO;
import dtos.InstructorDTO;
import errorhandling.NotFoundException;
import facades.CourseFacade;
import facades.YogaClassFacade;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import net.minidev.json.JSONObject;

@Path("yogaclass")
public class YogaclassResource {
    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final YogaClassFacade FACADE = YogaClassFacade.getYogaClassFacade(EMF);
    
    @GET
    @Path("get")
    @RolesAllowed({"instructor", "student"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllYogaClasses() throws NotFoundException {
        return GSON.toJson(FACADE.getAllYogaClasses());
    }
    
    @POST
    @Path("create")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createYogaClass(String jsonString) throws ParseException, NotFoundException{
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        int maxParticipants = json.get("maxParticipants").getAsInt();
        //Havde problemer med date, så alle nye yoga classes starte på den date de bliver oprettet
        //String startDate = json.get("startDate").getAsString();
        double price = json.get("price").getAsDouble();
        int courseId = json.get("courseId").getAsInt();
        int instructorId = json.get("instructorId").getAsInt();
        //DateFormat format = new SimpleDateFormat(startDate);
        //Date date = format.parse(startDate);
        List<InstructorDTO> instructorDTOs = new ArrayList<>();
        instructorDTOs.add(new InstructorDTO(instructorId));           
        return GSON.toJson(FACADE.createYogaClass(maxParticipants, new Date(), price, new CourseDTO(courseId), instructorDTOs));
    }
    
    @PUT
    @Path("edit")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editYogaClass(String jsonString) throws NotFoundException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    int yogaClassId = json.get("yogaClassId").getAsInt();
    int newMaxParticipants = json.get("newMaxParticipants").getAsInt();
    double newPrice = json.get("newPrice").getAsDouble();
    int newCourseId = json.get("newCourseId").getAsInt();
    return GSON.toJson(FACADE.editYogaClass(yogaClassId, newMaxParticipants, newPrice, new CourseDTO(newCourseId)));
    }
    
    @DELETE
    @Path("delete/{yogaClassId}")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteYogaClass(@PathParam("yogaClassId") int yogaClassId) throws NotFoundException {
        return GSON.toJson(FACADE.deleteYogaClass(yogaClassId));
    }
    
    @PUT
    @Path("addinstructor")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addInstructor(String jsonString) throws NotFoundException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    int yogaClassId = json.get("yogaClassId").getAsInt();
    int instructorId = json.get("instructorId").getAsInt();
    return GSON.toJson(FACADE.addInstructor(yogaClassId, new InstructorDTO(instructorId)));
    }
    
    @PUT
    @Path("removeinstructor")
    @RolesAllowed("instructor")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String removeInstructor(String jsonString) throws NotFoundException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    int yogaClassId = json.get("yogaClassId").getAsInt();
    int instructorId = json.get("instructorId").getAsInt();
    return GSON.toJson(FACADE.addInstructor(yogaClassId, new InstructorDTO(instructorId)));
    }
}
