package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import dtos.CourseDTO;
import dtos.YogaClassDTO;
import entities.Course;
import entities.Instructor;
import entities.Role;
import entities.SignedUp;
import entities.Student;
import entities.YogaClass;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import io.restassured.parsing.Parser;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import net.minidev.json.JSONObject;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

public class YogaclassResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private EntityManager em;
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    
    private Student s1, s2;
    private Instructor i1, i2;
    private Role r1, r2;
    private Course c1, c2;
    private YogaClass yc1, yc2;
    private SignedUp su1, su2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }
    
    public YogaclassResourceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void tearDownClass() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }
    
    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("SignedUp.deleteAllRows").executeUpdate();
            em.createNamedQuery("YogaClass.deleteAllRows").executeUpdate();
            em.createNamedQuery("Course.deleteAllRows").executeUpdate();
            em.createNamedQuery("Instructor.deleteAllRows").executeUpdate();
            em.createNamedQuery("Student.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            
            
            s1 = new Student("aaa", "aaa", "111", "a@b");
            s2 = new Student("bbb", "bbb", "222", "c@d");
            
            i1 = new Instructor("instr1", "111");
            i2 = new Instructor("instr2", "222");
            List<Instructor> instructors1 = new ArrayList<>();
            instructors1.add(i1);
            List<Instructor> instructors2 = new ArrayList<>();
            instructors2.add(i2);
            
            r1 = new Role("student");
            r2 = new Role("instructor");
            em.persist(r1);
            em.persist(r2);
            
            c1 = new Course("course1", "it is course 1");
            c2 = new Course("course2", "it is course 2");
            
            yc1 = new YogaClass(10, new Date(), 100, c1, instructors1);
            yc2 = new YogaClass(20, new Date(), 50, c2, instructors2);
            
            su1 = new SignedUp(true, yc1, s1);
            su2 = new SignedUp(false, yc2, s2);
            
            s1.signUp(su1);
            s2.signUp(su2);
            s1.addRole(r1);
            s2.addRole(r1);
            
            i1.addRole(r2);
            i2.addRole(r2);
            i1.addYogaClass(yc1);
            i2.addYogaClass(yc2);
            
            c1.addYogaClass(yc1);
            c2.addYogaClass(yc2);
            
            yc1.addSignedUp(su1);
            yc2.addSignedUp(su2);
            
            em.persist(c1);
            em.persist(c2);
            
            em.persist(i1);
            em.persist(i2);
            
            em.persist(yc1);
            em.persist(yc2);
            
            em.persist(s1);
            em.persist(s2);
         
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
        logOut();
    }
    
    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String username, String password, String role) {
        String json = String.format("{username: \"%s\", password: \"%s\", role: \"%s\"}", username, password, role);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetAllYogaClasses() throws Exception {
        login("aaa", "aaa", "student");
        
        with()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/yogaclass/get").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("maxParticipants", Matchers.containsInAnyOrder(20, 10));             
    }

 

    @Test
    public void testCreateYogaClass() {
        login("instr1", "111", "instructor");
        Date date = new Date();
        JSONObject yogaClassDTO = new JSONObject();
        yogaClassDTO.put("maxParticipants", 40);
        //yogaClassDTO.put("startDate", date.toString());
        yogaClassDTO.put("price", 55);
        yogaClassDTO.put("courseId", c1.getCourseId());
        yogaClassDTO.put("instructorId", i1.getInstructorId());
        
        YogaClassDTO result 
                = with()
                        .body(yogaClassDTO) //include object in body
                        .contentType("application/json")
                        .header("x-access-token", securityToken)
                        .when().request("POST", "/yogaclass/create").then() //post REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(YogaClassDTO.class); //extract result JSON as object
        
        assertNotNull(result.getYogaClassId());    
    }


    @Test
    public void testEditYogaClass() throws Exception {
        login("instr1", "111", "instructor");
        
        JSONObject yogaClassDTO = new JSONObject();
        yogaClassDTO.put("yogaClassId", yc1.getYogaClassId());
        yogaClassDTO.put("newMaxParticipants", 15);
        yogaClassDTO.put("newPrice", 85);
        yogaClassDTO.put("newCourseId", c2.getCourseId());
        
        YogaClassDTO result 
                = with()
                        .body(yogaClassDTO) //include object in body
                        .contentType("application/json")
                        .header("x-access-token", securityToken)
                        .when().request("PUT", "/yogaclass/edit").then() //put REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(YogaClassDTO.class); //extract result JSON as object
        
        assertEquals(result.getYogaClassId(), yc1.getYogaClassId());
    }

    @Test
    public void testDeleteYogaClass() throws Exception {
        login("instr1", "111", "instructor");
        
        YogaClassDTO result 
                = with()
                        .contentType("application/json")
                        .header("x-access-token", securityToken)
                        .when().request("DELETE", "/yogaclass/delete/" + yc1.getYogaClassId()).then() //delete REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(YogaClassDTO.class); //extract result JSON as object
        
        assertEquals(result.getYogaClassId(), yc1.getYogaClassId());
    }
    
    @Test
    public void testAddInstructor() throws Exception {
        login("instr1", "111", "instructor");
        
        JSONObject yogaClassDTO = new JSONObject();
        yogaClassDTO.put("yogaClassId", yc1.getYogaClassId());
        yogaClassDTO.put("instructorId", i2.getInstructorId());
       
        YogaClassDTO result 
                = with()
                        .body(yogaClassDTO) //include object in body
                        .contentType("application/json")
                        .header("x-access-token", securityToken)
                        .when().request("PUT", "/yogaclass/addinstructor").then() //put REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(YogaClassDTO.class); //extract result JSON as object
        
        assertEquals(2, result.getInstructors().size());
    }
    
    //@Test
    public void testRemoveInstructor() throws Exception {
        login("instr1", "111", "instructor");
        
        JSONObject yogaClassDTO = new JSONObject();
        yogaClassDTO.put("yogaClassId", yc1.getYogaClassId());
        yogaClassDTO.put("instructorId", i1.getInstructorId());
       
        YogaClassDTO result 
                = with()
                        .body(yogaClassDTO) //include object in body
                        .contentType("application/json")
                        .header("x-access-token", securityToken)
                        .when().request("PUT", "/yogaclass/removeinstructor").then() //put REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(YogaClassDTO.class); //extract result JSON as object
        
        assertEquals(0, result.getInstructors().size());
    }
    
}
