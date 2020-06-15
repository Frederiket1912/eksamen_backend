package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@NamedQuery(name = "Instructor.deleteAllRows", query = "DELETE from Instructor")
public class Instructor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer instructorId;
    @Column(name = "instructor_name", length = 25)
    private String name;
    @ManyToMany(mappedBy = "instructors")
    private List<YogaClass> yogaClasses = new ArrayList<>();
    private String password;
    @JoinTable(name = "instructor_roles", joinColumns = {
    @JoinColumn(name = "instructor_name", referencedColumnName = "instructor_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  @ManyToMany
  private List<Role> roleList = new ArrayList();

    public Instructor() {
    }

    public Instructor(String name, String password) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.yogaClasses = new ArrayList<>();
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
    
    public void addRole(Role role){
        this.roleList.add(role);
    }
    
    public void setUserPass(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, this.password);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    @Override
    public String toString() {
        return "Instructor{" + "instructorId=" + instructorId + ", name=" + name + ", yogaClasses=" + yogaClasses + '}';
    }
  
}
