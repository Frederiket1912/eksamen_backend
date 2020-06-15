package dtos;

import entities.SignedUp;
import entities.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentDTO {
    private Integer studentId;
    private String name;
    private String phone;
    private String email;
    private List<SignedUpDTO> signedUp = new ArrayList<>();

    public StudentDTO(Student student) {
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.phone = student.getPhone();
        this.email = student.getEmail();
        for (SignedUp su : student.getSignedUp()) {
            this.signedUp.add(new SignedUpDTO(su));
        }   
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<SignedUpDTO> getSignedUp() {
        return signedUp;
    }

    @Override
    public String toString() {
        return "StudentDTO{" + "studentId=" + studentId + ", name=" + name + ", phone=" + phone + ", email=" + email + ", signedUp=" + signedUp + '}';
    }
    
}
