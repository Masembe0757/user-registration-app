package org.pahappa.systems.registrationapp.services;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import java.util.Base64;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.views.UserView;
import org.pahappa.systems.registrationapp.dao.UserRegDao;

public class UserService {

    //Generic method to return date
    private static Date dateFormat(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(date);
    }

    private static boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
        final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false; // Password must be at least 8 characters long
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
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

    //Generic method to check if there are users in the database
    private static boolean usersAvailable(){
        List<User> usersList = UserRegDao.returnAllUsers();
        return !usersList.isEmpty();
    }
   public static String addUser(String firstName, String lastName, String userName, Date dateOfBirth,String password1,String password2, String email) {
        String error_message= "";
       User newUser = new User();
       if(firstName.isEmpty() || hasDigits(firstName) || hasSpecialCharacters(firstName) ){
           error_message = "First name field  has digits or special characters in it";
       }
       else if(hasDigits(lastName) || hasSpecialCharacters(lastName)){
           error_message = "Last name field has digits or special characters  in it";
       } else if ( userName.length() < 6 || hasSpecialCharacters(userName) ) {
           error_message = "User name field has special characters less than 6 or has special characters";

       } else if (Character.isDigit(userName.charAt(0))) {
           error_message = "User name field  can not start with a digit";

       } else if (onlyDigits(userName, userName.length())) {
           error_message = "User name field can not contain only digits";
       } else  if(!isValidEmail(email)){
           error_message = "Email provided is of incorrect format";
       }else if (!password1.equals(password2)) {
           error_message = "Passwords do not match";
       } else {
                User returnedUser = UserRegDao.returnUser(userName);
                if (returnedUser!= null) {
                    error_message ="User name already taken please enter new user name";
                } else {
                    if(!isValidPassword(password1)){
                        error_message = "Password must have more than 8 lowercase, uppercase, digits and special characters ";
                    }
                        if(dateOfBirth.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)) {

                            // Encrypting the password with BCrypt
                            String encodedPassword = Base64.getEncoder().encodeToString(password1.getBytes());


                            User user = new User();
                            user.setFirstname(firstName);
                            user.setLastname(lastName);
                            user.setUsername(userName);
                            user.setDateOfBirth(dateOfBirth);
                            user.setPassword(encodedPassword);
                            user.setEmail(email);
                            UserRegDao.saveUser(user);
                        }
                        else {
                            error_message = "Date provided is beyond current date, please enter correct date of birth bellow:";
                        }

                    }
       }
       return  error_message;

   }
   public static List<User> returnAllUsers()  {
       List<User> usersList = UserRegDao.returnAllUsers();
           return usersList;
       }

   public static User returnUserOfUserName(String userName)  {
       return UserRegDao.returnUser(userName);
   }
   public static String updateUserOfUserName(String firstName, String lastName, String userName, Date dateOfBirth,String password1,String password2, String email) {
       String error_message = "";
        if(!usersAvailable()) {
            error_message = "No users to update, system currently empty";
        }
        else {
                User returnedUser = UserRegDao.returnUser(userName);
                if (returnedUser!=null) {
                    //update validation

                    if( hasDigits(firstName) || hasSpecialCharacters(firstName) ){
                        error_message ="First name field has digits or special characters  in it";

                    }
                    else if(hasDigits(lastName) || hasSpecialCharacters(lastName)){
                        error_message ="Last name field has digits or special characters in it";

                    }else  if(!isValidEmail(email)){
                        error_message = "Email provided is of incorrect format";
                    } else if (!password1.equals(password2)) {
                        error_message = "Passwords do not match";
                    } else {
                        if(!isValidPassword(password1)){
                            error_message = "Password must have more than 8 lowercase, uppercase, digits and special characters";
                        }

                            if (dateOfBirth.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)){
                                // Encrypting the password with BCrypt
                                String encodedPassword = Base64.getEncoder().encodeToString(password1.getBytes());
                                UserRegDao.updateUser(firstName,lastName,userName,dateOfBirth,encodedPassword,email);
                            }else {
                                error_message = "Date provided is beyond current date";
                            }

                        }
                    }
                else {
                    error_message = "User not registered in the database";
                }
        }
        return  error_message;
   }
   public static  String deleteUserOfUserName(String userName){
        String error_message= "";
        if(usersAvailable()) {
            User returnedUser = UserRegDao.returnUser(userName);
                if (returnedUser!=null) {

                    //Deleting user
                    UserRegDao.deleteUser(userName);

                    //Deleting dependants attached to user
                    User user = UserService.returnUserOfUserName(userName);
                    List<Dependant> dependants =DependantService.getDependantsForUser(user.getId());
                    if(!dependants.isEmpty()){
                        DependantService.deleteAllDependantsForUser(user.getId());
                    }
                }
                else {
                   error_message = "The username supplied is not in the database";
                }
        }
        else {
            error_message = "No users in system to delete yet";
        }
        return  error_message;
   }
   public static List<User> returnUserOFName(String name){
       List<User> allUsers = UserService.returnAllUsers();
       List<User> returnedUsers = new ArrayList<>();
       for(User u : allUsers){
           if(u.getUsername().equals(name)){
               returnedUsers.add(u);
           } else if (u.getFirstname().equals(name)) {
               returnedUsers.add(u);
           } else if (u.getLastname().equals(name)) {
               returnedUsers.add(u);
           }
       }
       for(User u : returnedUsers){
           String decodedPassword = new String(Base64.getDecoder().decode(u.getPassword()));
           u.setPassword(decodedPassword);
       }
       return returnedUsers;
   }

    public static String softDeleteUserOfUserName(String userName) {
        String error_message= "";
        if(usersAvailable()) {
            User returnedUser = UserRegDao.returnUser(userName);
            if (returnedUser!=null) {
                UserRegDao.softDeleteUser(userName);
            }
            else {
                error_message = "The username supplied is not in the database";
            }
        }
        else {
            error_message = "No users in system to delete yet";
        }
        return  error_message;
    }

    public  void deleteAllUsers()  {
        String error_message ="";
       UserView userView = new UserView();

       if(usersAvailable()) {
           //Deleting all users
           UserRegDao.deleteAllUsers();
           //Deleting all dependants
           List<Dependant> dependants = DependantDao.returnDependants();
           if(!dependants.isEmpty()){
               DependantService.deleteAllDependants();
           }

       }else {
           error_message = "There are no users currently in the system";
       }
   }


}
