package facades;

import dtos.InstructorDTO;
import entities.Course;
import entities.Instructor;
import entities.Role;
import entities.Student;
import entities.YogaClass;
import errorhandling.AuthenticationException;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

public class InstructorFacade {
    private static EntityManagerFactory emf;
    private static InstructorFacade instance;
    
    private InstructorFacade(){}
    
    public static InstructorFacade getInstructorFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new InstructorFacade();
        }
        return instance;
    }
    
    public Instructor getVeryfiedInstructor(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Instructor instructor;
        try {
            //Kan give problemer hvis name ikke er unique, som det ikke er.
            TypedQuery<Instructor> q = em.createQuery("SELECT i FROM Instructor i WHERE i.name = :username", Instructor.class).setParameter("username", username);
            instructor = q.getSingleResult();
            if (instructor == null || !instructor.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return instructor;
    }
    
    public List<InstructorDTO> getAllInstructors() throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        List<Instructor> instructors = null;
        try {
            TypedQuery<Instructor> q = em.createQuery("SELECT i FROM Instructor i", Instructor.class);
            instructors = q.getResultList();
            if (instructors == null) throw new NotFoundException("No instructors found");
        }finally {
            em.close();
        }
        List<InstructorDTO> instructordtos = new ArrayList<>();
        for (Instructor i : instructors) {
            instructordtos.add(new InstructorDTO(i));
        }
        return instructordtos;
    }
    
    public InstructorDTO createInstructor(String name, String password) {
        EntityManager em = emf.createEntityManager();
        Instructor i = new Instructor(name, password);
        try {
            em.getTransaction().begin();
            Role role = em.find(Role.class, "instructor");
            i.addRole(role);
            em.persist(i);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new InstructorDTO(i);
    }
    
    public InstructorDTO editInstructor(int instructorId, String newName, String newPassword) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        Instructor i = null;
        try {
            i = em.find(Instructor.class, instructorId);
            if (null == i) throw new NotFoundException("No instructor found, could not be edited");
            i.setName(newName);
            i.setUserPass(newPassword);
            em.getTransaction().begin();
            em.persist(i);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new InstructorDTO(i);
    }
    
    public InstructorDTO deleteInstructor(int instructorId) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        Instructor i = null;
        try {
            i = em.find(Instructor.class, instructorId);
            if (null == i) throw new NotFoundException("No instructor found, could not be deleted");
            em.getTransaction().begin();
            //fjerner referencer fra yoga classes
            for (YogaClass yc : i.getYogaClasses()) {
                yc.removeInstructor(i);
                em.persist(yc);
            }
            em.remove(i);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new InstructorDTO(i);
    }
    
    public static void main(String[] args) throws NotFoundException {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        InstructorFacade IF = new InstructorFacade();
        //IF.createInstructor("Instructor1", "123");
        //IF.createInstructor("Instructor2", "123");
        //System.out.println(IF.getAllInstructors());
        
    }
    
}
