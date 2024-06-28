package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
        String message = DependantService.getDependantService().attachDependant(dateOfBirth,firstName,lastName,userName,gender,user_id);
        if(message.isEmpty()){
            if(getCurrentUser().getRole()==1){
                return "/pages/protected/dependants/dependants_all.xhtml";
            } else{
                return "/pages/protected/home/home_user.xhtml";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/dependants/register.xhtml";
        }
    }
    public String deleteDependant(String userName){
        String message = DependantService.getDependantService().deleteDependantsByUserName(userName);
        if(message.isEmpty()) {
            if(getCurrentUser().getRole()==1){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dependant deleted successfully", null));
                return  "/pages/protected/dependants/dependants_all.xhtml";
            } else{
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dependant deleted successfully", null));
                return  "/pages/protected/dependants/dependants.xhtml";
            }
        }else {
            if(getCurrentUser().getRole()==1){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,message, null));
                return  "/pages/protected/dependants/dependants_all.xhtml";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,message, null));
                return  "/pages/protected/dependants/dependants.xhtml";
            }
        }
    }
    public String updateDependant(String firstName, String lastName, String userName, Date dateOfBirth, String gender){
        String message = DependantService.getDependantService().updateDependantByUserName(firstName,lastName,userName,dateOfBirth,gender);
        if(message.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dependant updated successfully", null));
            if(getCurrentUser().getRole()==1){
                return  "/pages/protected/dependants/dependants_all.xhtml";
            } else if (getCurrentUser().getRole()==0) {
                return  "/pages/protected/dependants/dependants.xhtml";
            }

        }{
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/dependants/update_dependant.xhtml";
        }
    }
    public String back(){
        if(getCurrentUser().getRole()==1){
            return"/pages/protected/users/users.xhtml";
        }else {
            return"/pages/protected/home/home_user.xhtml";
        }
    }
    public String softDeleteDependant(String userName){
        String message = DependantService.getDependantService().softDeleteDependantsByUserName(userName);
        if(message.isEmpty()) {
            if(getCurrentUser().getRole()==1){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,"Dependant soft deleted successfully", null));
                return  "/pages/protected/dependants/dependants_all.xhtml?faces-redirect=true";
            } else if (getCurrentUser().getRole()==0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dependant soft deleted successfully", null));
                return  "/pages/protected/dependants/dependants.xhtml";
            }

        }else {
            if(getCurrentUser().getRole()==1){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
                return  "/pages/protected/dependants/dependants_all.xhtml?faces-redirect=true";
            } else if (getCurrentUser().getRole()==0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
                return  "/pages/protected/dependants/dependants.xhtml";
            }

        }
        return "";

    }
    public int softDeleteCount(){
        List<Dependant> dependants = DependantDao.getDependantDao().returnDependants();
        int softCount = 0;
        for(Dependant dependant: dependants){
            if(dependant.getDeleted_at()==null){
                
            }else{softCount++;}
        }
        return softCount;
    }
    public int dependantCount(){
        List<Dependant> dependants = DependantDao.getDependantDao().returnDependants();
        int depCount = 0;
        for(Dependant dependant:dependants){
            depCount++;
        }
        return  depCount;
    }

    public String previous() {
        if(getCurrentUser().getRole()==1){
            return"/pages/protected/dependants/dependants_all.xhtml";
        }
        else {
            return "/pages/protected/dependants/dependants.xhtml&id="+getCurrentUser().getId();
        }
    }

    public String deleteAllDependants() {
        String message = DependantService.getDependantService().deleteAllDependants();
        if(message.isEmpty()){
            return "/pages/protected/dependants/dependants_all.xhtml";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/dependants/dependants_all.xhtml";
        }
    }

    public String deleteAllDependantsOfUser(int userId) throws ParseException {
        List<Dependant> userDependants = DependantService.getDependantService().getDependantsForUser(userId);
        if(userDependants.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User has no dependants", null));
            return "/pages/protected/dependants/dependants.xhtml";
        }
        else {
            DependantService.getDependantService().deleteAllDependantsForUser(userId);
            return "/pages/protected/dependants/dependants.xhtml";
        }
    }
}
