package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "saveuserbean")
@SessionScoped
public class SaveUserBean implements Serializable {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String password;
    private String email;
    private String password1;
    private String password2;

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SaveUserBean() {
    }
    private User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public String saveUser(String firstName, String lastName, String userName, Date dateOfBirth, String pass1,String pass2, String email) {

        String message = UserService.addUser(firstName,lastName,userName,dateOfBirth,pass1,pass2,email);
        if(message.isEmpty()) {
            return  "/pages/protected/users/users.xhtml?faces-redirect=true";
            //Emailing



        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/users/register.xhtml";
        }
    }

    public String deleteUser(String userName){
        String message = UserService.deleteUserOfUserName(userName);
        if(message.isEmpty()) {
            return  "/pages/protected/users/users.xhtml?faces-redirect=true";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/users/users.xhtml";
        }

    }
    public String updateUser(String firstName, String lastName, String userName, Date dateOfBirth, String password1,String password2, String email) {
        String message = UserService.updateUserOfUserName(firstName, lastName, userName, dateOfBirth, password1,password2, email);
        if(message.isEmpty()) {
            if(getCurrentUser().getRole()==1){
                return  "/pages/protected/users/users.xhtml?faces-redirect=true";
            }else if(getCurrentUser().getRole()==0){
                return  "/pages/protected/users/user_details.xhtml?faces-redirect=true";
            }
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/users/update_user.xhtml";
        }
        return "";
    }
    public String softDeleteUser(String userName) {
        String message = UserService.softDeleteUserOfUserName(userName);
        if(message.isEmpty()) {
            return  "/pages/protected/users/users.xhtml?faces-redirect=true";
        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return "/pages/protected/users/users.xhtml";
        }
    }
    public int softDeleteCount() {
        List<User> users = UserService.returnAllUsers();
        int softCount = 0;
        for(User user: users){
            if(user.getDeleted_at()==null){
                
            }else{softCount++;}
        }
        return softCount;

    }
    public int userCount(){
        int userNumber =0 ;
        List<User> users = UserService.returnAllUsers();
        for(User user:users){
            userNumber++;
        }
        return userNumber;
    }


    public String getCurrentUserName() {
        return getCurrentUser().getUsername();
    }

    public int getCurrentId() {
        return getCurrentUser().getId();
    }

    public String deleteAllUsers() {
            String message = UserService.deleteAllUsers();
            if(message.isEmpty()){
                return "/pages/protected/users/users.xhtml";
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
                return "/pages/protected/users/users.xhtml" ;
            }


    }

    public boolean isAdmin() {
        return getCurrentUser().getRole() == 1;
    }
}