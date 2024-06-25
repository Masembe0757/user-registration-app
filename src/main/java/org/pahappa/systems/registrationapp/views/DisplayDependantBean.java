package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.services.DependantService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScoped
@ManagedBean(name = "dependantdisplaybean")
public class DisplayDependantBean implements Serializable {
    private int user_id;
    private String attribute;
    private String gender;
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public DisplayDependantBean(){}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<Dependant> dependants(int user_id){
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(user_id);
        List<Dependant> dependantList = new ArrayList<>();
        for(Dependant dependant : dependants){
            if(dependant.getWd()==0){
                dependantList.add(dependant);
            }
        }
        return  dependantList;
    }
    public List<Dependant> getAllDependants(){
        List<Dependant> dependants =  DependantDao.returnDependants();
        List<Dependant> dependantList = new ArrayList<>();
        for(Dependant dependant:dependants){
            if(dependant.getWd()==0){
                dependantList.add(dependant);
            }
        }
        return dependantList;
    }
    public List<Dependant> searchDependantByName(String name){
        List<Dependant> returneddependants = DependantService.getDependantsByName(name);
        List<Dependant> dependantList = new ArrayList<>();
        for(Dependant dependant:returneddependants){
            if(dependant.getWd()==0){
                dependantList.add(dependant);
            }
        }

        return  dependantList;
    }



}
