package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.UserRegDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.exception.RandomException;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.views.UserView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DependantService {
    private static boolean genderOption(String gender){
        boolean correctGender = false;
        if(gender.equals("male")){
            correctGender =true;
        }
        else if (gender.equals("female")){
            correctGender=true;
        }
        else {
            System.out.println("Gender not accepted");
        }
        return correctGender;
    }

    //Generic method to check date format
    private static Date dateFormat(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println();
        return dateFormat.parse(formatter.parse(date).toString().split(" ")[3]);

    }
    //Generic method to check if username has only digits
    private static boolean onlyDigits(String str, int n)
    {
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) < '0'
                    || str.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    private static boolean hasSpecialCharacters(String s){
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
    private static boolean hasDigits(String str){
        boolean hasDigits = false;
        for(int i =0 ; i < str.length(); i++){
            if(Character.isDigit(str.charAt(i))){
                hasDigits =  true;
            }
        }
        return hasDigits;
    }

    public static void getDependantsForUser(User returnedUser) throws RandomException, ParseException {
        List<Dependant> user_dependants = DependantDao.returnDependantsForUserId(returnedUser.getId());
        if(user_dependants.isEmpty()){
            throw new RandomException("No dependants available for this user");
        }
        else {
            System.out.println("This user has the following dependants");
            for(Dependant d : user_dependants){
                System.out.println(d.toString());
            }
            //going back to menu
            UserService.returnUserOfUserName(returnedUser.getUsername());
        }
    }

    public  static void attachDependant(User returnedUser) throws ParseException, RandomException {
        UserView userView = new UserView();
        Dependant dependant = new Dependant();
        System.out.println("Enter Dependandant's first name");
        String firstName = userView.Scan();
        System.out.println("Enter Dependandant's last name");
        String lastName = userView.Scan();
        System.out.println("Enter Dependandant's user name");
        String userName = userView.Scan();
        System.out.println("Enter Dependandant's date of birth");
        String dateOfBirth = userView.Scan();
        System.out.println("Enter Dependandant's gender");
        String gender = userView.Scan();

        if(firstName.isEmpty() || hasDigits(firstName) || hasSpecialCharacters(firstName) ){
            UserView.Print("First name field missing or has digits or special characters in it, please refill the field below :");
            attachDependant(returnedUser);
        }
        else if(hasDigits(lastName) || hasSpecialCharacters(lastName)){
            UserView.Print("Last name field has digits or special characters  in it, please refill the field correctly below :");
           attachDependant(returnedUser);
        } else if (userName.isEmpty() || userName.length() < 6 || hasSpecialCharacters(userName) ) {
            UserView.Print("User name field missing or characters less than 6 or has special characters , please refill the field correctly below :");
            attachDependant(returnedUser);
        } else if (Character.isDigit(userName.charAt(0))) {
            UserView.Print("User name field  can not start with a digit, please refill the field correctly below :");
            attachDependant(returnedUser);
        } else if (onlyDigits(userName, userName.length())) {
            UserView.Print("User name field can not contain only digits, please refill the field correctly below :");
            attachDependant(returnedUser);
        } else if(dateOfBirth.isEmpty()) {
            UserView.Print("Date of birth field missing, please refill the field correctly below with correct format:");
            attachDependant(returnedUser);
        } else if (!genderOption(gender)) {
            UserView.Print("Gender provided is invalid, please enter correct gender below :");
            attachDependant(returnedUser);
        } else {
            Date dateOfBirthParsed = null;
                try {
                     dateOfBirthParsed = dateFormat(dateOfBirth);
                }catch (Exception e){
                    throw  new RandomException("Date provided is of incorrect format yyyy-mm-dd ,invalid month of the year or invalid day of the month, please refill the field correctly below :");
                }

            if(dateOfBirthParsed.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)) {

                dependant.setFirstname(firstName);
                dependant.setLastname(lastName);
                dependant.setUsername(userName);
                dependant.setGender(gender);
                dependant.setDateOfBirth(dateOfBirthParsed);

                //attaching dependant to user
                returnedUser.getDependant().add(dependant);
                dependant.setUser(returnedUser);
                DependantDao.saveDependant(dependant);
                //going back to menu
                UserService.returnUserOfUserName(returnedUser.getUsername());
            }
            else{
                throw new RandomException("Date provided is a future date, please enter a valid date below :");
            }
        }

    }

    public static void getDependantsByGender(User user) throws ParseException, RandomException {
        UserView userView =new UserView();
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(user.getId());
        System.out.println("Enter F for female or M for male dependants");
        String gender_option = userView.Scan();
            if (!dependants.isEmpty()) {
                if (gender_option.equals("F")) {
                    System.out.println("User has the following female dependants");
                    boolean femalesPresent = false;

                    for (Dependant d : dependants) {
                        if (d.getGender().equals("female")) {
                            femalesPresent =true;
                            System.out.println(d.toString());
                        }
                    }
                    if(!femalesPresent){
                        throw  new RandomException("User has no female dependants");
                    }
                    UserService.returnUserOfUserName(user.getUsername());

                } else {
                    System.out.println("User has the following male dependants");
                    boolean malesPresent = false;
                    for (Dependant d : dependants) {
                        if (d.getGender().equals("male")) {
                            malesPresent =true;
                            System.out.println(d.toString());
                        }
                    }
                    if(!malesPresent){
                        throw  new RandomException("User has no male dependants");
                    }
                    UserService.returnUserOfUserName(user.getUsername());
                }

            } else {
                throw  new RandomException("User has no dependants to filter");
            }
    }

    public static void getDependantsByName(User returnedUser ) throws RandomException, ParseException {
        UserView userView = new UserView();
        System.out.println("Enter dependant's  user name, first name or last name");
        String Name = userView.Scan();

        List<Dependant> dependants = DependantDao.returnDependantsForUserId(returnedUser.getId());
        if(!dependants.isEmpty()) {
            boolean dependantPresent = false;
            for (Dependant d : dependants) {
                if (d.getUsername().equals(Name)) {
                    System.out.println(d.toString());
                    dependantPresent = true;
                } else if (d.getFirstname().equals(Name)) {
                    System.out.println(d.toString());
                    dependantPresent = true;

                } else if (d.getLastname().equals(Name)) {
                    System.out.println(d.toString());
                    dependantPresent = true;
                }
            }
            if (!dependantPresent) {
                System.out.println("No dependant with those details is attached to this user");
            }
            UserService.returnUserOfUserName(returnedUser.getUsername());
        }else {
            throw  new RandomException("No dependants to return");
        }
    }

    public static void deleteDependantsByUserName(User returnedUSer) throws ParseException, RandomException {
        UserView userView = new UserView();
        System.out.println("Enter user name for dependant you wish to delete");
        String uName = userView.Scan();
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(returnedUSer.getId());
        boolean dependantPresent = false;
        if(!dependants.isEmpty()) {
            for (Dependant d : dependants) {
                if (d.getUsername().equals(uName)) {
                    DependantDao.deleteDependant(uName);
                    dependantPresent = true;
                }
            }
            if (!dependantPresent) {
                System.out.println("No dependant with those details is attached to this user");
            }
            UserService.returnUserOfUserName(returnedUSer.getUsername());
        }else {
            throw new RandomException("This user has no dependants to delete ");
        }

    }

    public static void updateDependantByUserName(User returnedUser) throws ParseException, RandomException {
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(returnedUser.getId());
        UserView userView =new UserView();
        boolean dependantPresent = false;
        System.out.println("Enter user name of dependant you wish to update");
        String userName = userView.Scan();

        if(!dependants.isEmpty()){
            for(Dependant d : dependants){
                if(d.getUsername().equals(userName)){
                    dependantPresent = true;
                    System.out.println("Enter Dependandant's new first name");
                    String firstNameNew = userView.Scan();
                    System.out.println("Enter Dependandant's new last name");
                    String lastNameNew = userView.Scan();
                    System.out.println("Enter Dependandant's new date of birth formart \"yyyy-mm-dd\" ");
                    String dateOfBirthNew = userView.Scan();
                    System.out.println("Enter Dependandant's new gender \"female\" or \"male\" ");
                    String genderNew = userView.Scan();



                    if(firstNameNew.isEmpty() || hasDigits(firstNameNew) || hasSpecialCharacters(firstNameNew) ){
                        UserView.Print("First name field missing or has digits or special characters in it, please refill the field below :");
                        updateDependantByUserName(returnedUser);
                    }
                    else if(hasDigits(lastNameNew) || hasSpecialCharacters(lastNameNew)){
                        UserView.Print("Last name field has digits or special characters  in it, please refill the field correctly below :");
                        updateDependantByUserName(returnedUser);
                    } else if(dateOfBirthNew.isEmpty()) {
                        UserView.Print("Date of birth field missing, please refill the field correctly below with correct format:");
                        updateDependantByUserName(returnedUser);
                    } else if (!genderOption(genderNew)) {
                        UserView.Print("Gender provided is invalid, please enter correct gender below :");
                        updateDependantByUserName(returnedUser);
                    } else {
                        Date dateOfBirthParsedNew = null;
                        try {
                            dateOfBirthParsedNew = dateFormat(dateOfBirthNew);
                        }catch (Exception e){
                            throw  new RandomException("Date provided is of incorrect format yyyy-mm-dd ,invalid month of the year or invalid day of the month, please refill the field correctly below :");
                        }

                        if(dateOfBirthParsedNew.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)) {
                            DependantDao.updateDependant(firstNameNew,lastNameNew,userName,dateOfBirthParsedNew,genderNew);
                        }
                        else{
                            throw new RandomException("Date provided is a future date, please enter a valid date below :");
                        }
                    }
                }
            }
            if(!dependantPresent){
                System.out.println("User name provided does not belong to any dependant");
            }
            UserService.returnUserOfUserName(returnedUser.getUsername());

        }else {
            throw  new RandomException("No dependants to update");
        }
    }

    public static void deleteAllDependants(User returnedUser) throws RandomException {
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(returnedUser.getId());
        UserView userView = new UserView();
        if(!dependants.isEmpty()){
            UserView.Print("Are you sure you want to delete all Dependants? 1-yes 0-no");
            int confirmation = Integer.parseInt(userView.Scan());
            if(confirmation==1){
                int outCome = DependantDao.deleteDependants();
                if(outCome >0 ) {
                    UserView.Print("All users have been deleted successfully");
                }
                else {
                    throw new RandomException("Delete operation failed");
                }
            }else {
                throw new RandomException("Operation cancelling successful, thanks for using our system");
            }
        }
        else {
            throw  new RandomException("User has no dependants to delete");
        }

    }
}
