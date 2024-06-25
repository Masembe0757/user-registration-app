package org.pahappa.systems.registrationapp.models;
import javax.persistence.*;
@Entity
@Table(name = "dependant_table")
public class Dependant extends Account{
    private String gender;
    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Dependant names  :" + this.getFirstname() + " " + this.getLastname() + ", gender : " +this.getGender();
    }
}