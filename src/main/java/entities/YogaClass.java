package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
@NamedQuery(name = "YogaClass.deleteAllRows", query = "DELETE from YogaClass")
public class YogaClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer yogaClassId;
    private int maxParticipants;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    private double price;
    @OneToMany(mappedBy = "yogaClass")
    private List<SignedUp> signedUps = new ArrayList<>();
    @ManyToOne
    private Course course;
    @ManyToMany
    private List<Instructor> instructors = new ArrayList<>();

    public YogaClass() {
    }

    public YogaClass(int maxParticipants, Date startDate, double price, Course course, List<Instructor> instructors) {
        this.maxParticipants = maxParticipants;
        this.startDate = startDate;
        this.price = price;
        this.course = course;
        this.instructors = instructors;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void addInstructor(Instructor instructor) {
        this.instructors.add(instructor);
    }
    
    public void removeInstructor(Instructor instructor) {
        Instructor instructorToBeDeleted = new Instructor();
        for (Instructor i : this.instructors){
            if (i.getInstructorId().equals(instructor.getInstructorId())) {
                instructorToBeDeleted = i;
            }
        }
        this.instructors.remove(instructorToBeDeleted);
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<SignedUp> getSignedUps() {
        return signedUps;
    }

    public void addSignedUp(SignedUp signedUp) {
        this.signedUps.add(signedUp);
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getYogaClassId() {
        return yogaClassId;
    }

    public void setYogaClassId(Integer yogaClassId) {
        this.yogaClassId = yogaClassId;
    }

    @Override
    public String toString() {
        return "YogaClass{" + "yogaClassId=" + yogaClassId + ", maxParticipants=" + maxParticipants + ", startDate=" + startDate + ", price=" + price + ", signedUps=" + signedUps + ", course=" + course + ", instructors=" + instructors + '}';
    }

    

}
