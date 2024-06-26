package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@ManagedBean
@RequestScoped
public class loginbean {
    private String username;
    private String entry;
    private int id;
    private  int role;


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PostConstruct
    public void init() {
        User admin = new User();
        admin.setFirstname("manager");
        admin.setLastname("manager");
        admin.setUsername("manager");
        admin.setDateOfBirth(new Date());
        admin.setPassword(Base64.getEncoder().encodeToString("manager".getBytes()));
        admin.setEmail("manager@gmail.com");
        admin.setRole(1);
        User user = UserRegDao.returnUser(admin.getUsername());
        if(user==null){
            UserRegDao.createAdmin(admin);
        }

    }


    public String login(String userName, String passWord) {
        User user = UserRegDao.returnUser(userName);
        if (user != null) {
            String decodedPassword = new String(Base64.getDecoder().decode(user.getPassword()));
            if(decodedPassword.equals(entry)){
                if(user.getRole() == 0){
                    FacesContext context = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = context.getExternalContext();
                    externalContext.getSessionMap().put("currentUser", user);
                    return "/pages/protected/home/home_user.xhtml";
                }
                else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = context.getExternalContext();
                    externalContext.getSessionMap().put("currentUser", user);
                    return "/pages/protected/home/home.xhtml";
                }

            } else if (user.getEmail().equals(entry)) {
                if(user.getRole() == 0){
                    FacesContext context = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = context.getExternalContext();
                    externalContext.getSessionMap().put("currentUser", user);
                    return "/pages/protected/home/home_user.xhtml";
                }
                else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ExternalContext externalContext = context.getExternalContext();
                    externalContext.getSessionMap().put("currentUser", user);
                    return "/pages/protected/home/home.xhtml";
                }
            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password/email", null));
                return "/pages/protected/user/login.xhtml";
            }



        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User does not exist in database", null));
            return "/pages/protected/user/login.xhtml";
        }
    }

    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.getSessionMap().put("currentUser", null);
        externalContext.redirect(externalContext.getRequestContextPath() +"/pages/login/login.xhtml");
    }
}
