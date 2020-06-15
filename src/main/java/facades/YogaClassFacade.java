package facades;

import dtos.CourseDTO;
import dtos.InstructorDTO;
import dtos.YogaClassDTO;
import entities.Course;
import entities.Instructor;
import entities.SignedUp;
import entities.YogaClass;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

public class YogaClassFacade {
    private static EntityManagerFactory emf;
    private static YogaClassFacade instance;
    
    private YogaClassFacade(){}
    
    public static YogaClassFacade getYogaClassFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new YogaClassFacade();
        }
        return instance;
    }
    
    public List<YogaClassDTO> getAllYogaClasses() throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        List<YogaClass> yogaClasses = null;
        try {
            TypedQuery<YogaClass> q = em.createQuery("SELECT y FROM YogaClass y", YogaClass.class);
            yogaClasses = q.getResultList();
            if (yogaClasses == null) throw new NotFoundException("No yoga classes found");
        }finally {
            em.close();
        }
        List<YogaClassDTO> yogaClassdtos = new ArrayList<>();
        for (YogaClass yc : yogaClasses) {
            yogaClassdtos.add(new YogaClassDTO(yc));
        }
        return yogaClassdtos;
    }
    
    public YogaClassDTO createYogaClass(int maxParticipants, Date startDate, double price, CourseDTO course, List<InstructorDTO> instructors) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        YogaClass yc; 
        try {
            em.getTransaction().begin();
            Course c = em.find(Course.class, course.getCourseId());
            if (c == null) throw new NotFoundException("Course could not be found, yoga class could not be created");           
            List<Instructor> instruct = new ArrayList<>();
            //loop for at lave instructorDTOs om til instructors
            for (InstructorDTO i : instructors) {
                Instructor instr = em.find(Instructor.class, i.getInstructorId());
                if (instr == null) throw new NotFoundException("Instructor could not be found, yoga class could not be created");
                instruct.add(instr);
            }
            yc = new YogaClass(maxParticipants, startDate, price, c, instruct);
            //instructors får en reference til den nye yoga class
            for (InstructorDTO i : instructors) {
                Instructor instr = em.find(Instructor.class, i.getInstructorId());
                if (instr == null) throw new NotFoundException("Instructor could not be found, yoga class could not be created");
                instr.addYogaClass(yc);
                em.persist(instr);
            }
            
            //Course får en reference til den nye yoga class
            c.addYogaClass(yc);
            em.persist(c);
            em.persist(yc);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new YogaClassDTO(yc);
    }
    
    public YogaClassDTO editYogaClass(int yogaClassId, int newMaxParticipants, double newPrice, CourseDTO newCourse) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        YogaClass yc = null;
        try {
            em.getTransaction().begin();
            yc = em.find(YogaClass.class, yogaClassId);
            //fjerner reference fra den gamle course
            if (null == yc) throw new NotFoundException("No yoga class found, could not be edited");
            Course oldCourse = yc.getCourse();
            oldCourse.removeYogaClass(yc);
            em.persist(oldCourse);
            
            //tilføjer reference til den nye course
            Course newCourse_ = em.find(Course.class, newCourse.getCourseId());
            newCourse_.addYogaClass(yc);
            em.persist(newCourse_);
            
            //fjerner reference til de gamle instructors
            List<Instructor> oldInstructors = yc.getInstructors();
            for (Instructor oldInstructor : oldInstructors) {
                oldInstructor.removeYogaClass(yc);
                em.persist(oldInstructor);
            }
            
            yc.setMaxParticipants(newMaxParticipants);
            yc.setPrice(newPrice);
            yc.setCourse(newCourse_);     
            em.persist(yc);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new YogaClassDTO(yc);
    }
    
    public YogaClassDTO deleteYogaClass(int yogaClassId) throws NotFoundException{
        EntityManager em = emf.createEntityManager();
        YogaClass yc = null;
        try {
            yc = em.find(YogaClass.class, yogaClassId);
            if (null == yc) throw new NotFoundException("No yoga class found, could not be deleted");
            em.getTransaction().begin();
            //fjerner reference fra Course
            Course c = yc.getCourse();
            c.removeYogaClass(yc);
            em.persist(c);
            
            //fjerner referencer fra instructors 
            for (Instructor i : yc.getInstructors()) {
                i.removeYogaClass(yc);
                em.persist(i);
            }
            
            //fjerner referencer fra signed ups
            for (SignedUp su : yc.getSignedUps()) {
                //Er måske nok en bedre løsning end at sætte yogaclass til null, 
                //især burde man måske tjekke om der skulle ske noget tilbagebetaling,
                //hvis det ikke er en afsluttet class, men det er lidt ud over hvad jeg når på en dag
                su.setYogaClass(null);
                em.persist(su);
            }
            
            em.remove(yc);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return new YogaClassDTO(yc);
    }
    
    //burde tjekke om instructoren allerede er tilknyttet denne yoga class, så den samme ikke kan komme på flere gange
    public YogaClassDTO addInstructor(int yogaClassId, InstructorDTO instructor) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        YogaClass yc = null;
        try {
            em.getTransaction().begin();
            yc = em.find(YogaClass.class, yogaClassId);
            if (null == yc) throw new NotFoundException("No yoga class found, could not add instructor");
            
            //tilføjere reference til instructor
            Instructor i = em.find(Instructor.class, instructor.getInstructorId());
            i.addYogaClass(yc);
            em.persist(i);
            
            yc.addInstructor(i);
            em.persist(yc);
            em.getTransaction().commit();            
        }finally {
            em.close();
        }
        return new YogaClassDTO(yc);
    }
    
    public YogaClassDTO removeInstructor(int yogaClassId, InstructorDTO instructor) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        YogaClass yc = null;
        try {
            em.getTransaction().begin();
            yc = em.find(YogaClass.class, yogaClassId);
            if (null == yc) throw new NotFoundException("No yoga class found, could not remove instructor");
            
            //fjerner reference fra instructor
            Instructor i = em.find(Instructor.class, instructor.getInstructorId());
            i.removeYogaClass(yc);
            em.persist(i);
            
            yc.removeInstructor(i);
            em.persist(yc);
            em.getTransaction().commit();            
        }finally {
            em.close();
        }
        return new YogaClassDTO(yc);
    }
    
    public static void main(String[] args) throws NotFoundException {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        YogaClassFacade yf = new YogaClassFacade();
        Course c = new Course("gwkihi", "efq");
        c.setCourseId(1);
//        CourseDTO courseDTO = new CourseDTO(c);
       // Instructor i = new Instructor("awaw", "qcfqd");
        //i.setInstructorId(2);
//        List<InstructorDTO> instructors = new ArrayList<>();
//        instructors.add(new InstructorDTO(i));
//        yf.createYogaClass(30, new Date(), 100, courseDTO, instructors);
        yf.editYogaClass(1, 70 ,50, new CourseDTO(c));
        //yf.deleteYogaClass(2);
        //yf.addInstructor(1, i);
        //yf.removeInstructor(1, i);
    }
    
}
