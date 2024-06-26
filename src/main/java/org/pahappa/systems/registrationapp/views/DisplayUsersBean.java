package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.Account;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@SessionScoped
@ManagedBean(name = "displayusersbean")
public class DisplayUsersBean implements Serializable {
    private List<User> users = new ArrayList<User>();
    private String name;
    private String password;
    private String email;
    private int role;
    private int id;
    private String username;
    private String firstname;

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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    private String lastname;
    private Date dateOfBirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisplayUsersBean(){

    }
    private User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public List<User> get_users(){
        List<User> users_list = new ArrayList<>();
        users = UserService.returnAllUsers();
        if(users.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "System currently has no users", null));
        }
        else {
            for(User user: users) {
                if(user.getWd()==0) {
                    users_list.add(user);
                }
            }
        }
        return  users_list;
    }
    public List<User> searchUserByName(String name){
        List<User> returnedUsers = UserService.returnUserOFName(name);
        List<User> userList = new ArrayList<>();
        if(returnedUsers.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No user found for that name", null));
        }
        for(User user: returnedUsers)
            if(user.getWd()==0){
              userList.add(user);
            }
        return userList;
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String back() {
        if(getCurrentUser().getRole()==1){
            return "/pages/protected/home/home.xhtml";
        }else {
            return "/pages/protected/home/home_user.xhtml";
        }
    }
}
