package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@NamedQuery(name = "Student.deleteAllRows", query = "DELETE from Student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;
    @Column(name = "student_name", length = 25)
    private String name;
    private String phone;
    private String email;
    @JoinTable(name = "student_roles", joinColumns = {
        @JoinColumn(name = "student_name", referencedColumnName = "student_name")}, inverseJoinColumns = {
        @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany
    private List<Role> roleList = new ArrayList();
    private String password;
    @OneToMany(mappedBy = "student", cascade=CascadeType.PERSIST)
    private List<SignedUp> signedUp = new ArrayList<>();

    public Student(String name, String password, String phone, String email) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.phone = phone;
        this.email = email;
    }

    public Student() {
    }
    
    public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList();
    for (Role role : roleList) {
      rolesAsStrings.add(role.getRoleName());
    }
    return rolesAsStrings;
  }

    public void signUp(SignedUp signedUp) {
        this.signedUp.add(signedUp);
    }

    public List<SignedUp> getSignedUp() {
        return signedUp;
    }

    public void setUserPass(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    
    public void addRole(Role role){
        this.roleList.add(role);
    }

    @Override
    public String toString() {
        return "Student{" + "studentId=" + studentId + ", name=" + name + ", phone=" + phone + ", email=" + email + ", roleList=" + roleList + ", password=" + password + ", signedUp=" + signedUp + '}';
    }

}
