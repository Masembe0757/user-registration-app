package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.exception.RandomException;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "savedependantbean")
@SessionScoped
public class SaveDependantBean implements Serializable {
    private int user_id;
    private String attribute;
    private String gender;
    private int id;
    private String username;
    private  int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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

    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    public  SaveDependantBean(){}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    private User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }
    public String saveDependant(Date dateOfBirth, String firstName, String lastName,String userName,String gender,int user_id){
        String message = DependantService.attachDependant(dateOfBirth,firstName,lastName,userName,gender,user_id);
        if(message.isEmpty()){
            if(getCurrentUser().getRole()==1){
                return "/pages/dependants/dependants_all.xhtml";
            } else if (getCurrentUser().getRole() == 0) {
                return "/pages/dependants/dependants.xhtml?id="+user_id;
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/dependants/register.xhtml?faces-redirect=true";
        }
        return "";

    }
    public String deleteDependant(String userName,int userId){
        String message = DependantService.deleteDependantsByUserName(userName);

        if(message.isEmpty()) {
            if(getCurrentUser().getRole()==1){
                return  "/pages/dependants/dependants_all.xhtml";
            } else if (getCurrentUser().getRole()==0) {
                return  "/pages/dependants/dependants.xhtml?id="+userId;
            }

        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/dependants/dependants_all.xhtml";
        }
        return "";

    }
    public String updateDependant(String firstName, String lastName, String userName, Date dateOfBirth, String gender){
        String message = DependantService.updateDependantByUserName(firstName,lastName,userName,dateOfBirth,gender);
        if(message.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dependant updated successfully", null));
            if(getCurrentUser().getRole()==1){
                return  "/pages/dependants/dependants_all.xhtml";
            } else if (getCurrentUser().getRole()==0) {
                return  "/pages/dependants/dependants.xhtml";
            }

        }{
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/dependants/update_dependant.xhtml";
        }
    }
    public String back(){
        if(getCurrentUser().getRole()==1){
            return"/pages/users/users.xhtml";
        }else {
            return"/pages/home/home_user.xhtml";
        }
    }
    public String softDeleteDependant(String userName,int userId){
        String message = DependantService.softDeleteDependantsByUserName(userName);
        if(message.isEmpty()) {
            if(getCurrentUser().getRole()==1){
                return  "/pages/dependants/dependants_all.xhtml?faces-redirect=true";
            } else if (getCurrentUser().getRole()==0) {
                return  "/pages/dependants/dependants.xhtml?id="+userId;
            }

        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/dependants/dependants_all.xhtml";
        }
        return "";

    }
    public int softDeleteCount(){
        List<Dependant> dependants = DependantDao.returnDependants();
        int softCount = 0;
        for(Dependant dependant: dependants){
            if(dependant.getWd()==1){
                softCount++;
            }
        }
        return softCount;
    }
    public int dependantCount(){
        List<Dependant> dependants = DependantDao.returnDependants();
        int depCount = 0;
        for(Dependant dependant:dependants){
            depCount++;
        }
        return  depCount;
    }

    public String previous() {
        if(getCurrentUser().getRole()==1){
            return"/pages/dependants/dependants_all.xhtml";
        }
        else {
            return "/pages/dependants/dependants.xhtml&id="+getCurrentUser().getId();
        }
    }

    public String getDeleteAllDependants() {
        String message = DependantService.deleteAllDependants();
        if(message.isEmpty()){
            return "/pages/dependants/dependants_all.xhtml";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/dependants/dependants_all.xhtml";
        }
    }

    public String deleteAllDependantsOfUser(int userId) throws ParseException {
        List<Dependant> userDependants = DependantService.getDependantsForUser(userId);
        if(userDependants.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User has no dependants", null));
            return "/pages/dependants/dependants.xhtml";
        }
        else {
            DependantService.deleteAllDependantsForUser(userId);
            return "/pages/dependants/dependants.xhtml";
        }
    }
}
