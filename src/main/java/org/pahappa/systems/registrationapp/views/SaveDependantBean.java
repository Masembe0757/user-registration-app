package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.Account;
import org.pahappa.systems.registrationapp.models.Dependant;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;

@ManagedBean(name = "savedependantbean")
@SessionScoped
public class SaveDependantBean extends Dependant {
    private int user_id;
    public  SaveDependantBean(){}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String saveDependant(Date dateOfBirth, String firstName, String lastName,String userName,String gender,int user_id){
        Dependant dependant = new Dependant();
        dependant.setFirstname(firstName);
        dependant.setLastname(lastName);
        dependant.setUsername(userName);
        dependant.setGender(gender);
        dependant.setDateOfBirth(dateOfBirth);

        //attaching dependant to user
        UserRegDao.returnUserofId(user_id).getDependant().add(dependant);
        dependant.setUser(UserRegDao.returnUserofId(user_id));
        DependantDao.saveDependant(dependant);
        return "/pages/dependants/dependants.xhtml?id=user_id";
    }
    public void deleteDependant(String userName){
        DependantDao.deleteDependant(userName);
    }


}
