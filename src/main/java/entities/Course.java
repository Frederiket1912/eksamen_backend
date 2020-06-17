package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "Course.deleteAllRows", query = "DELETE from Course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    private String courseName;
    private String descrition;
    @OneToMany(mappedBy = "course")
    private List<YogaClass> yogaClasses = new ArrayList<>();

    public Course(String courseName, String descrition) {
        this.courseName = courseName;
        this.descrition = descrition;
    }

    public Course() {
    }

    public List<YogaClass> getYogaClasses() {
        return yogaClasses;
    }

    public void addYogaClass(YogaClass yogaClass) {
        this.yogaClasses.add(yogaClass);
    }

    public void removeYogaClass(YogaClass yogaClass) {
        YogaClass ycToBeDeleted = new YogaClass();
        for (YogaClass yc : this.yogaClasses) {
            if (Objects.equals(yc.getYogaClassId(), yogaClass.getYogaClassId())) {
                ycToBeDeleted = yc;
            }
        }
        this.yogaClasses.remove(ycToBeDeleted);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Course{" + "courseId=" + courseId + ", courseName=" + courseName + ", descrition=" + descrition + ", yogaClasses=" + yogaClasses + '}';
    }

}
