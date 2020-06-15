package facades;

import dtos.SignedUpDTO;
import dtos.StudentDTO;
import entities.Course;
import entities.Instructor;
import entities.Role;
import entities.SignedUp;
import entities.Student;
import entities.YogaClass;
import errorhandling.AuthenticationException;
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

public class StudentFacadeTest {
    private static EntityManagerFactory emf;
    private static StudentFacade facade;
    private EntityManager em;
    
    private Student s1, s2;
    private Instructor i1, i2;
    private Role r1, r2;
    private Course c1, c2;
    private YogaClass yc1, yc2;
    private SignedUp su1, su2;
    
    public StudentFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = StudentFacade.getStudentFacade(emf);
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
     * Test of getVeryfiedStudent method, of class StudentFacade.
     */
    @Test
    public void testGetVeryfiedStudentWithRightPassword() throws Exception {
        Student s = facade.getVeryfiedStudent("aaa", "aaa");
        assertNotNull(s);
    }
    
    /**
     * Test of getVeryfiedStudent method, of class StudentFacade.
     */
    @Test
    public void testGetVeryfiedStudentWithWrongPassword() throws Exception {
        Exception exception = assertThrows(AuthenticationException.class, () -> {
        Student s = facade.getVeryfiedStudent("aaa", "bbb");
    });
        assertEquals("Invalid user name or password", exception.getMessage());
    }
    

    /**
     * Test of createStudent method, of class StudentFacade.
     */
    @Test
    public void testCreateStudent() {
        assertNotNull(facade.createStudent("new name", "new pw", "1234", "new mail"));
    }

    /**
     * Test of SignUpToClass method, of class StudentFacade.
     */
    @Test
    public void testSignUpToClass() throws Exception {
        SignedUpDTO suDTO = facade.SignUpToClass(s1.getStudentId(), yc2.getYogaClassId(), true);
        assertEquals("aaa", suDTO.getStudent());
        
    }
    
}
