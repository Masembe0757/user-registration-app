package org.pahappa.systems.registrationapp.models;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import  java.util.*;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "user_table")
public class User extends Account {
    public User(){}

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List< Dependant> dependant = new ArrayList<>();

    public List<Dependant> getDependant() {
        return dependant;
    }

    public void setDependant(List<Dependant> dependant) {
        this.dependant = dependant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(dependant, user.dependant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dependant);
    }
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return "User : " + this.getUsername()+ " has  full name : " + this.getFirstname() + ' ' + this.getLastname() + ' ' + " and a date of birth : " + df.format(this.getDateOfBirth());
    }
}
