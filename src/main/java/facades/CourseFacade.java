package facades;

import dtos.CourseDTO;
import entities.Course;
import entities.YogaClass;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

public class CourseFacade {
    private static EntityManagerFactory emf;
    private static CourseFacade instance;
    
    private CourseFacade(){}
    
    public static CourseFacade getCourseFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CourseFacade();
        }
        return instance;
    }
    
    public List<CourseDTO> getAllCourses() throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        List<CourseDTO> coursesdto = new ArrayList<>();
        try {
            TypedQuery<Course> q = em.createQuery("SELECT c FROM Course c", Course.class);
            List<Course> courses = q.getResultList();
            if (courses == null) throw new NotFoundException("No courses found");
            for (Course c : courses) {
                coursesdto.add(new CourseDTO(c));
            }
        }finally {
            em.close();
        }
        return coursesdto;
    }
    
    public CourseDTO createCourse(String courseName, String description) {
        EntityManager em = emf.createEntityManager();
        Course c = new Course(courseName, description);
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new CourseDTO(c);
    }
    
    public CourseDTO editCourse(int courseId, String newName, String newDescription) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        Course c = null;
        try {
            c = em.find(Course.class, courseId);
            if (null == c) throw new NotFoundException("No course found, could not be edited");
            c.setCourseName(newName);
            c.setDescrition(newDescription);
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new CourseDTO(c);
    }
    
    public int deleteCourse(int courseId) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        Course c = null;
        try {
            c = em.find(Course.class, courseId);
            if (null == c) throw new NotFoundException("No course found, could not be deleted");
            em.getTransaction().begin();
            //fjerner referencer fra yoga classes
            for (YogaClass yc : c.getYogaClasses()) {
                yc.setCourse(null);
                em.persist(yc);
            }
            em.remove(c);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return c.getCourseId();
    }
    
    public static void main(String[] args) throws NotFoundException {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        CourseFacade cf = new CourseFacade();
        //cf.createCourse("TestCourse1", "description");
        //cf.createCourse("TestCourse2", "description2");
        //System.out.println(cf.getAllCourses());
        //cf.editCourse(1, "edited course", "edited description");
        cf.deleteCourse(6);
        
    }
}
