package facades;

import dtos.CourseDTO;
import entities.Course;
import entities.Instructor;
import entities.Role;
import entities.SignedUp;
import entities.Student;
import entities.YogaClass;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;


public class CourseFacadeTest {
    private static EntityManagerFactory emf;
    private static CourseFacade facade;
    private EntityManager em;
    
    private Student s1, s2;
    private Instructor i1, i2;
    private Role r1, r2;
    private Course c1, c2;
    private YogaClass yc1, yc2;
    private SignedUp su1, su2;
    
    public CourseFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = CourseFacade.getCourseFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
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
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAllCourses method, of class CourseFacade.
     */
    @Test
    public void testGetAllCourses() throws Exception {
        assertEquals(2, facade.getAllCourses().size());
    }

    /**
     * Test of createCourse method, of class CourseFacade.
     */
    @Test
    public void testCreateCourse() {
        assertNotNull(facade.createCourse("Course 3", "created course").getCourseId());
    }

    /**
     * Test of editCourse method, of class CourseFacade.
     */
    @Test
    public void testEditCourse() throws Exception {
        CourseDTO c = facade.editCourse(c1.getCourseId(), "edited name", "edited description");
        assertEquals(c1.getCourseId(), c.getCourseId());
        List<CourseDTO> result = facade.getAllCourses();
        assertTrue((result.stream().anyMatch(courseDTO -> courseDTO.getCourseName().equals("edited name"))));
    }

    /**
     * Test of deleteCourse method, of class CourseFacade.
     */
    @Test
    public void testDeleteCourse() throws Exception {
        facade.deleteCourse(c1.getCourseId());
        assertEquals(1, facade.getAllCourses().size());
    }

    
}
