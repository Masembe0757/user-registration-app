package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.Account;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "saveuserbean")
@SessionScoped
public class SaveUserBean extends Account {


    public SaveUserBean(){}
    public String saveUser(String firstName, String lastName, String userName, Date dateOfBirth){
        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setUsername(userName);
        user.setDateOfBirth(dateOfBirth);
        UserRegDao.saveUser(user);
        return "/pages/users/users.xhtml?faces-redirect=true";
    }
    public void deleteUser(String userName){
        UserRegDao.deleteUser(userName);
    }

}
