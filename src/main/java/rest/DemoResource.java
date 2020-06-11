package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ChuckDTO;
import dtos.CombinedDTO;
import dtos.DTOInterface;
import dtos.DadDTO;
import dtos.DogImgDTO;
import dtos.SkyscannerDTO;
import dtos.WeatherDTO;
import dtos.WeatherDTO;
import entities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("test")
    @RolesAllowed({"admin", "user"})
    public String getThingsFromMultipleAPIs() throws InterruptedException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ChuckDTO chuckDTO = new ChuckDTO("https://api.chucknorris.io/jokes/random");
        DadDTO dadDTO = new DadDTO("https://icanhazdadjoke.com");
        DogImgDTO diDTO = new DogImgDTO("https://dog.ceo/api/breeds/image/random");
        SkyscannerDTO scannerDTO = new SkyscannerDTO();
        WeatherDTO weatherDTO = new WeatherDTO();

        List<DTOInterface> dtos = new ArrayList<>();
        dtos.add(chuckDTO);
        dtos.add(dadDTO);
        dtos.add(diDTO);
        dtos.add(scannerDTO);
        dtos.add(weatherDTO);
        
        ExecutorService workingJack = Executors.newFixedThreadPool(5);
        for (DTOInterface dto : dtos) {
        Runnable task  = new Runnable() {
                @Override
                public void run() {
                    dto.fetch();
                }
            };
        workingJack.submit(task);
        }
        
        workingJack.shutdown();
        workingJack.awaitTermination(15, TimeUnit.SECONDS);
        CombinedDTO combinedDTO = new CombinedDTO(dadDTO, chuckDTO, diDTO, weatherDTO, scannerDTO);
        //This is what your endpoint should return       
        String combinedJSON = gson.toJson(combinedDTO);
        return combinedJSON;

    }

}
