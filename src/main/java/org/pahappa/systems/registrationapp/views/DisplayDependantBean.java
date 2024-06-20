package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@ManagedBean(name = "dependantdisplaybean")
public class DisplayDependantBean extends Dependant {
    private int user_id;
    public DisplayDependantBean(){}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<Dependant> dependants(int user_id){
        return  DependantDao.returnDependantsForUserId(user_id);
    }
    public List<Dependant> getAllDependants(){
        return  DependantDao.returnDependants();
    }

}
