package org.pahappa.systems.registrationapp.models;
import javax.persistence.*;
@Entity
@Table(name = "dependant_table")
public class Dependant extends Account{
    public enum Gender {
        male,
        female,
    }
    private Gender gender;
    @ManyToOne
    private User user;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "Dependant names  :" + this.getFirstname() + " " + this.getLastname() + ", gender : " +this.getGender();
    }
}