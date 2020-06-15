package dtos;

import entities.SignedUp;
import entities.YogaClass;
import java.util.Date;


public class SignedUpDTO {
    private Integer signedUpId;
    private boolean payed;
    private Date datePayed;
    private String student;
    private YogaClassDTO yogaClass;

    public SignedUpDTO(SignedUp signedUp) {
        this.signedUpId = signedUp.getSignedUpId();
        this.payed = signedUp.isPayed();
        this.datePayed = signedUp.getDatePayed();
        this.student = signedUp.getStudent().getName();
        this.yogaClass = new YogaClassDTO(signedUp.getYogaClass());
    }

    public Integer getSignedUpId() {
        return signedUpId;
    }

    public boolean isPayed() {
        return payed;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    public String getStudent() {
        return student;
    }

    public YogaClassDTO getYogaClass() {
        return yogaClass;
    }

    @Override
    public String toString() {
        return "SignedUpDTO{" + "signedUpId=" + signedUpId + ", payed=" + payed + ", datePayed=" + datePayed + ", student=" + student + ", yogaClass=" + yogaClass + '}';
    }
}
