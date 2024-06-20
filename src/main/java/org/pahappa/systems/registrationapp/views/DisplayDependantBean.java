package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@ManagedBean(name = "dependantdisplaybean")
public class DisplayDependantBean extends Dependant {
    private int user_id;
    private String attribute;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

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
    public List<Dependant> searchDependantByName(String name){
        List<Dependant> returneddependants = new ArrayList<>();
        List<Dependant> dependants = DependantDao.returnDependants();
        for(Dependant d : dependants){
            if(d.getUsername().equals(name)){
                returneddependants.add(d);
            } else if (d.getFirstname().equals(name)) {
                returneddependants.add(d);
            } else if (d.getLastname().equals(name)) {
                returneddependants.add(d);
            } else if (d.getGender().equals(name)) {
                returneddependants.add(d);
            }
        }
        return returneddependants;
    }

}
