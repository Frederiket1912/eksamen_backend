package utils;

import entities.Course;
import entities.Instructor;
import entities.Role;
import entities.SignedUp;
import entities.Student;
import entities.YogaClass;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import startcodeStuff.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

    public static void main(String[] args) {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
        User user = new User("", "");
        User admin = new User("", "");
        User both = new User("", "");

        if (admin.getUserPass().equals("test") || user.getUserPass().equals("test") || both.getUserPass().equals("test")) {
            throw new UnsupportedOperationException("You have not changed the passwords");
        }

//    user.addRole(userRole);
//    admin.addRole(adminRole);
//    both.addRole(userRole);
//    both.addRole(adminRole);
//    em.persist(user);
//    em.persist(admin);
//    em.persist(both);
//    System.out.println("PW: " + user.getUserPass());
//    System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
//    System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
//    System.out.println("Created TEST Users");
        em.getTransaction().begin();
        Student s1 = new Student("Student Bo", "123", "111", "a@b");
        Student s2 = new Student("Student Bent", "123", "222", "c@d");

        Instructor i1 = new Instructor("Instructor Karl", "111");
        Instructor i2 = new Instructor("Instructor Frank", "222");
        List<Instructor> instructors1 = new ArrayList<>();
        instructors1.add(i1);
        List<Instructor> instructors2 = new ArrayList<>();
        instructors2.add(i2);

        Role r1 = new Role("student");
        Role r2 = new Role("instructor");
        em.persist(r1);
        em.persist(r2);

        Course c1 = new Course("Rytmisk Yoga", "det er rytmisk");
        Course c2 = new Course("Yoga for begyndere", "det er for begyndere");

        YogaClass yc1 = new YogaClass(10, new Date(), 100, c1, instructors1);
        YogaClass yc2 = new YogaClass(20, new Date(), 50, c2, instructors2);

        SignedUp su1 = new SignedUp(true, yc1, s1);
        SignedUp su2 = new SignedUp(false, yc2, s2);

        s1.signUp(su1);
        s2.signUp(su2);
        s1.addRole(r1);
        s2.addRole(r1);

        i1.addRole(r2);
        i2.addRole(r2);
        i1.addYogaClass(yc1);
        i2.addYogaClass(yc2);

        c1.addYogaClass(yc1);
        c2.addYogaClass(yc2);

        yc1.addSignedUp(su1);
        yc2.addSignedUp(su2);

        em.persist(c1);
        em.persist(c2);

        em.persist(i1);
        em.persist(i2);

        em.persist(yc1);
        em.persist(yc2);

        em.persist(s1);
        em.persist(s2);
        em.getTransaction().commit();

    }

}
