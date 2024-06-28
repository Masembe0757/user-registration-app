package org.pahappa.systems.registrationapp.services;
import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DependantService {
    DependantService dependantService;

    public DependantService getDependantService() {
        return dependantService;
    }

    public void setDependantService(DependantService dependantService) {
        this.dependantService = dependantService;
    }

    //Generic method to check if username has only digits
    private boolean onlyDigits(String str, int n)
    {
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) < '0'
                    || str.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean hasSpecialCharacters(String s){
        boolean hasCharacter = false;
        for (int i = 0; i < s.length(); i++) {
            // Checking the character for not being a letter,digit or space
            if (!Character.isDigit(s.charAt(i))
                    && !Character.isLetter(s.charAt(i))
                    && !Character.isWhitespace(s.charAt(i))) {
                hasCharacter =true;

            }
        }
        return hasCharacter;
    }
    //Generic method to check if name provided has digits in it
    private  boolean hasDigits(String str){
        boolean hasDigits = false;
        for(int i =0 ; i < str.length(); i++){
            if(Character.isDigit(str.charAt(i))){
                hasDigits =  true;
            }
        }
        return hasDigits;
    }

    public   List<Dependant> getDependantsForUser(int userId){
        List<Dependant> user_dependants = DependantDao.returnDependantsForUserId(userId);
        return  user_dependants;
    }

    public  String attachDependant(Date dateOfBirth, String firstName, String lastName,String userName,String gender,int user_id) {
        Dependant dependantReturned = DependantDao.returnDependant(userName);
        String error_message = "";

        if(dependantService.hasDigits(firstName) || dependantService.hasSpecialCharacters(firstName) ){
            error_message = "First name field has digits or special characters in it";
        } else if (dependantReturned!=null) {
            error_message = "User name already taken please chose a different one";
        } else if(dependantService.hasDigits(lastName) || dependantService.hasSpecialCharacters(lastName)){
            error_message = "Last name field has digits or special characters  in it";
        } else if (userName.length() < 6 || dependantService.hasSpecialCharacters(userName) ) {
            error_message = "User name field has characters less than 6 or has special characters ";
        } else if (Character.isDigit(userName.charAt(0))) {
            error_message = "User name field  can not start with a digit, please refill the field correctly below";
        } else if (dependantService.onlyDigits(userName, userName.length())) {
            error_message ="User name field can not contain only digits, please refill the field correctly below :";
        }  else {
            if(dateOfBirth.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)) {

                Dependant dependant = new Dependant();
                dependant.setFirstname(firstName);
                dependant.setLastname(lastName);
                dependant.setUsername(userName);
                dependant.setGender(Dependant.Gender.valueOf(gender));
                dependant.setDateOfBirth(dateOfBirth);

                //attaching dependant to user
                UserRegDao.returnUserofId(user_id).getDependant().add(dependant);
                dependant.setUser(UserRegDao.returnUserofId(user_id));
                DependantDao.saveDependant(dependant);
            }
            else{
                error_message= "Date of birth provided is a future date";
            }
        }
        return error_message;

    }

    public  List<Dependant> getDependantsByName(String Name ){

        List<Dependant> dependants = DependantDao.returnDependants();
        List<Dependant> deps = new ArrayList<>();
        if(!dependants.isEmpty()) {
            for (Dependant d : dependants) {
                if (d.getUsername().equalsIgnoreCase(Name)) {
                    deps.add(d);
                } else if (d.getFirstname().equalsIgnoreCase(Name)) {
                    deps.add(d);
                } else if (d.getLastname().equalsIgnoreCase(Name)) {
                    deps.add(d);
                } else if (d.getGender().toString().equalsIgnoreCase(Name)) {
                    deps.add(d);
                }
            }
        }
        return  deps;
    }

    public  String deleteDependantsByUserName(String uName) {
        List<Dependant> dependants = DependantDao.returnDependants();
        String error_message= "";
        if(!dependants.isEmpty()) {
            for(Dependant d: dependants){
                if ((d.getUsername().equals(uName))){
                    DependantDao.deleteDependant(uName);
                    error_message = "Dependant deleted successfully";
                }else {
                    error_message = "Dependant provided not in the database";
                }
            }

        }
        else {
            error_message = "No Dependants in system to delete yet";
        }
        return  error_message;

    }

    public  String updateDependantByUserName(String firstName, String lastName, String userName, Date dateOfBirth, String gender){
        String error_message = "";
        if(dependantService.hasDigits(firstName) || dependantService.hasSpecialCharacters(firstName) ){
            error_message = "First name field  has digits or special characters in it";
        }
        else if(dependantService.hasDigits(lastName) || dependantService.hasSpecialCharacters(lastName)){
            error_message = "Last name field has digits or special characters  in it, please refill the field correctly below :";
        } else {
            if (dateOfBirth.getYear() + 1900 < Calendar.getInstance().get(Calendar.YEAR)) {
                DependantDao.updateDependant(firstName, lastName, userName, dateOfBirth, gender);
            } else {
                error_message = "Date of birth provided is a future date";
            }
        }
        return error_message;
    }

    public void deleteAllDependantsForUser(int userId)  {
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(userId);
        String error_message = "";
        if(!dependants.isEmpty()){
                DependantDao.deleteDependants();
        }
        else {
            error_message = "User has no dependants to delete";
        }

    }

    public  String softDeleteDependantsByUserName(String uName) {
        List<Dependant> dependants = DependantDao.returnDependants();
        String error_message= "";
        if(!dependants.isEmpty()) {
            for(Dependant d: dependants){
                if ((d.getUsername().equals(uName))){
                    DependantDao.softDeleteDependant(uName);
                }else {
                    error_message = "Dependant provided not in the database";
                }
            }

        }
        else {
            error_message = "No Dependants in system to delete yet";
        }
        return  error_message;
    }

    public String deleteAllDependants() {
        List<Dependant> dependants = DependantDao.returnDependants();
        String error = "";
        if(dependants.isEmpty()){
            error = "No dependants to delete";
        }else {
            DependantDao.deleteDependants();
        }
        return error;

    }
}
