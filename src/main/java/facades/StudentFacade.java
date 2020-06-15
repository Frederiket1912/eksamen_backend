package facades;

import dtos.SignedUpDTO;
import dtos.StudentDTO;
import entities.Instructor;
import entities.Role;
import entities.SignedUp;
import entities.Student;
import startcodeStuff.User;
import entities.YogaClass;
import errorhandling.AuthenticationException;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

public class StudentFacade {
    private static EntityManagerFactory emf;
    private static StudentFacade instance;
    
    private StudentFacade(){}
    
    public static StudentFacade getStudentFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new StudentFacade();
        }
        return instance;
    }
    
    public Student getVeryfiedStudent(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Student student;
        try {
            //Kan give problemer hvis name ikke er unique, som det ikke er.
            TypedQuery<Student> q = em.createQuery("SELECT s FROM Student s WHERE s.name = :username", Student.class).setParameter("username", username);
            student = q.getSingleResult();
            if (student == null || !student.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return student;
    }
    
    public StudentDTO createStudent(String name, String password, String phone, String email){
        EntityManager em = emf.createEntityManager();
        Student student = new Student(name, password, phone, email);
        try {
            Role role = em.find(Role.class, "student");
            student.addRole(role);
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new StudentDTO(student);
    }
    
    public SignedUpDTO SignUpToClass(int studentId, int yogaClassId, boolean payed) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        SignedUp su = null;
        try {
            em.getTransaction().begin();
            Student student = em.find(Student.class, studentId);
            if (student == null) throw new NotFoundException("Student not found");
            YogaClass yc = em.find(YogaClass.class, yogaClassId);
            if (yc == null) throw new NotFoundException("Yoga Class not found");
            su = new SignedUp(payed, yc, student);            
            student.signUp(su);
            yc.addSignedUp(su);
            em.persist(student);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SignedUpDTO(su);
    }
    
    public static void main(String[] args) throws NotFoundException {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        StudentFacade sf = new StudentFacade();
        //sf.createStudent("testname", "123", "123", "testemail");
        System.out.println(sf.SignUpToClass(1, 4, false));
    }
}
