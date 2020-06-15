package dtos;

import entities.Course;
import entities.YogaClass;
import java.util.ArrayList;

import java.util.List;

public class CourseDTO {
    private Integer courseId;
    private String courseName;
    private String description;
    private List<YogaClassDTO> yogaClasses = new ArrayList<>();

    public CourseDTO(Course course) {
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
        this.description = course.getDescrition();
        for (YogaClass yc : course.getYogaClasses()) {
        this.yogaClasses.add(new YogaClassDTO(yc));
        }
    }

    public CourseDTO(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public List<YogaClassDTO> getYogaClasses() {
        return yogaClasses;
    }

    @Override
    public String toString() {
        return "CourseDTO{" + "courseId=" + courseId + ", courseName=" + courseName + ", description=" + description + ", yogaClasses=" + yogaClasses + '}';
    }
    
    
    
    
}
