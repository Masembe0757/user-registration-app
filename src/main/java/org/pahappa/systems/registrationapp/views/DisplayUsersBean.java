package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;
@SessionScoped
@ManagedBean(name = "displayusersbean")
public class DisplayUsersBean {
    private List<User> users = new ArrayList<User>();
    public DisplayUsersBean(){

    }

    public List<User> get_users(){
        users = UserRegDao.returnAllUsers();
        return users;
    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
