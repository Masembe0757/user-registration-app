package org.pahappa.systems.registrationapp.views;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.services.DependantService;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScoped
@ManagedBean(name = "dependantdisplaybean")
public class DisplayDependantBean implements Serializable {
    private int user_id;
    private String attribute;
    private String gender;
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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


    public List<Dependant> dependantsForUser(String attr,int userId) {
        List<Dependant> dependants = DependantService.getDependantsForUser(userId);
        List<Dependant> dependantList = new ArrayList<>();
        if(attr.isEmpty()) {
            for (Dependant dependant : dependants) {
                if (dependant.getDeleted_at() == null) {
                    dependantList.add(dependant);
                }
            }
        }else {
            for (Dependant dependant: dependants){
                if(dependant.getUsername().equalsIgnoreCase(attr) || dependant.getLastname().equalsIgnoreCase(attr) || dependant.getFirstname().equalsIgnoreCase(attr) ||dependant.getGender().equalsIgnoreCase(attr)   ){
                    dependantList.add(dependant);
                }
            }
        }
        return  dependantList;
    }
    public List<Dependant> searchDependantByName(String name) {
        List<Dependant> dependants = DependantDao.returnDependants();
        List<Dependant> dependantList = new ArrayList<>();

        if (name.isEmpty()) {
            dependantList = dependants;
        } else {
            List<Dependant> returneddependants = DependantService.getDependantsByName(name);

            for (Dependant dependant : returneddependants) {
                if (dependant.getDeleted_at() == null) {
                    dependantList.add(dependant);
                }
            }
        }
        return dependantList;
    }


    //Generic method to return date
    public String dateFormat(Date date) {
        if(date==null){
            return "";
        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
    }


}
