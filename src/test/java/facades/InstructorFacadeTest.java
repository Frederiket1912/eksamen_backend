package facades;

import dtos.InstructorDTO;
import entities.Course;
import entities.Instructor;
import entities.Role;
import entities.SignedUp;
import entities.Student;
import entities.YogaClass;
import errorhandling.AuthenticationException;
import errorhandling.NotFoundException;
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

public class InstructorFacadeTest {
    private static EntityManagerFactory emf;
    private static InstructorFacade facade;
    private EntityManager em;
    
    private Student s1, s2;
    private Instructor i1, i2;
    private Role r1, r2;
    private Course c1, c2;
    private YogaClass yc1, yc2;
    private SignedUp su1, su2;
    
    public InstructorFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = InstructorFacade.getInstructorFacade(emf);
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
     * Test of getVeryfiedInstructor method, of class InstructorFacade.
     */
    @Test
    public void testGetVeryfiedInstructorRightPassword() throws Exception {
        Instructor i = facade.getVeryfiedInstructor("instr1", "111");
        assertNotNull(i);
    }
    
    /**
     * Test of getVeryfiedInstructor method, of class InstructorFacade.
     */
    @Test
    public void testGetVeryfiedInstructorWrongPassword() throws Exception {
        Exception exception = assertThrows(AuthenticationException.class, () -> {
        Instructor i = facade.getVeryfiedInstructor("instr1", "11123");
    });
        assertEquals("Invalid user name or password", exception.getMessage());
    }

    /**
     * Test of getAllInstructors method, of class InstructorFacade.
     */
    @Test
    public void testGetAllInstructors() throws Exception {
        assertEquals(2, facade.getAllInstructors().size());
    }

    /**
     * Test of createInstructor method, of class InstructorFacade.
     */
    @Test
    public void testCreateInstructor() throws NotFoundException {
        assertNotNull(facade.createInstructor("Instr3", "333"));
        assertEquals(3, facade.getAllInstructors().size());
    }

    /**
     * Test of editInstructor method, of class InstructorFacade.
     */
    @Test
    public void testEditInstructor() throws Exception {
        InstructorDTO i = facade.editInstructor(i1.getInstructorId(), "Instructor3", "555");
        assertEquals(i1.getInstructorId(), i.getInstructorId());
        List<InstructorDTO> result = facade.getAllInstructors();
        assertTrue((result.stream().anyMatch(instructorDTO -> instructorDTO.getName().equals("Instructor3"))));
    }

    /**
     * Test of deleteInstructor method, of class InstructorFacade.
     */
    @Test
    public void testDeleteInstructor() throws Exception {
        facade.deleteInstructor(i1.getInstructorId());
        assertEquals(1, facade.getAllInstructors().size());
    }

    
}
