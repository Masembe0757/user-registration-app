package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.Account;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;
@SessionScoped
@ManagedBean(name = "displayusersbean")
public class DisplayUsersBean extends Account {
    private List<User> users = new ArrayList<User>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisplayUsersBean(){

    }

    public List<User> get_users(){
        users = UserRegDao.returnAllUsers();
        return users;
    }
    public List<User> searchUserByName(String name){
        List<User> returnedUsers = new ArrayList<>();
        List<User> allUsers = UserRegDao.returnAllUsers();
        for(User u : allUsers){
            if(u.getUsername().equals(name)){
                returnedUsers.add(u);
            } else if (u.getFirstname().equals(name)) {
                returnedUsers.add(u);
            } else if (u.getLastname().equals(name)) {
                returnedUsers.add(u);
            }
        }
        return returnedUsers;
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
