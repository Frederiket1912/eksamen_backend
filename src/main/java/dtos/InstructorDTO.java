package dtos;

import entities.Instructor;
import entities.YogaClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorDTO {
    private Integer instructorId;
    private String name;
    private Map<Integer, String> yogaClasses = new HashMap<Integer, String>();

    public InstructorDTO(Instructor instructor) {
        this.instructorId = instructor.getInstructorId();
        this.name = instructor.getName();
        for (YogaClass yc : instructor.getYogaClasses()) {
        this.yogaClasses.put(yc.getYogaClassId(), yc.getCourse().getCourseName());
        }
    }

    public InstructorDTO(Integer instructorId) {
        this.instructorId = instructorId;
    }
    

    public Integer getInstructorId() {
        return instructorId;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, String> getYogaClasses() {
        return yogaClasses;
    }


    @Override
    public String toString() {
        return "InstructorDTO{" + "instructorId=" + instructorId + ", name=" + name + ", yogaClasses=" + yogaClasses + '}';
    }
    
}
