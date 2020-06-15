package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

@Entity
@NamedQuery(name = "SignedUp.deleteAllRows", query = "DELETE from SignedUp")
public class SignedUp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer signedUpId;
    private boolean payed;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePayed;
    @ManyToOne
    private Student student;
    @ManyToOne(cascade=CascadeType.PERSIST)
    private YogaClass yogaClass;

    public SignedUp(boolean payed, YogaClass yogaClass, Student student) {
        this.payed = payed;
        this.yogaClass = yogaClass;
        this.student = student;
        if (payed) this.datePayed = new Date();
    }

    public SignedUp() {
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
        if (payed) this.datePayed = new Date();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public YogaClass getYogaClass() {
        return yogaClass;
    }

    public void setYogaClass(YogaClass yogaClass) {
        this.yogaClass = yogaClass;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    public Integer getSignedUpId() {
        return signedUpId;
    }

    public void setSignedUpId(Integer signedUpId) {
        this.signedUpId = signedUpId;
    }

    @Override
    public String toString() {
        return "SignedUp{" + "signedUpId=" + signedUpId + ", payed=" + payed + ", datePayed=" + datePayed + ", student=" + student + ", yogaClass=" + yogaClass + '}';
    }
    
    

}
