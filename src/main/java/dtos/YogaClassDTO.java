package dtos;

import entities.Instructor;
import entities.SignedUp;
import entities.YogaClass;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YogaClassDTO {
    private Integer yogaClassId;
    private int maxParticipants;
    private Date startDate;
    private double price;
    private Map<Integer, String> signedUps = new HashMap<Integer, String>();
    private String course;
    private List<InstructorDTO> instructors = new ArrayList<>();

    public YogaClassDTO(YogaClass yogaClass) {
        this.yogaClassId = yogaClass.getYogaClassId();
        this.maxParticipants = yogaClass.getMaxParticipants();
        this.startDate = yogaClass.getStartDate();
        this.price = yogaClass.getPrice();
        for (SignedUp su : yogaClass.getSignedUps()){
        this.signedUps.put(su.getSignedUpId(), su.getStudent().getName());
        }
        this.course = yogaClass.getCourse().getCourseName();
        for (Instructor i : yogaClass.getInstructors()) {
        this.instructors.add(new InstructorDTO(i));
        }
    }

    public Integer getYogaClassId() {
        return yogaClassId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Date getStartDate() {
        return startDate;
    }

    public double getPrice() {
        return price;
    }

    public Map<Integer, String> getSignedUps() {
        return signedUps;
    }

    public String getCourse() {
        return course;
    }

    public List<InstructorDTO> getInstructors() {
        return instructors;
    }

    @Override
    public String toString() {
        return "YogaClassDTO{" + "yogaClassId=" + yogaClassId + ", maxParticipants=" + maxParticipants + ", startDate=" + startDate + ", price=" + price + ", signedUps=" + signedUps + ", course=" + course + ", instructors=" + instructors + '}';
    }
    
}
