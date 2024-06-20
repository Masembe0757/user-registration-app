package org.pahappa.systems.registrationapp.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.pahappa.systems.registrationapp.exception.RandomException;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.views.UserView;
import org.pahappa.systems.registrationapp.dao.UserRegDao;

public class UserService {


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

    //Generic method to check date format
    private static Date dateFormat(String date) throws ParseException {
        DateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println();
        return dateFormat.parse(formatter.parse(date).toString().split(" ")[3]);

    }

    //Generic method to check if there are users in the database
    private static boolean usersAvailable(){
        List<User> usersList = UserRegDao.returnAllUsers();
        return !usersList.isEmpty();
    }
   public static void addUser(String firstName, String lastName, String userName, String dateOfBirth) {
       User newUser = new User();
       UserView userView = new UserView();
       if(firstName.isEmpty() || hasDigits(firstName) || hasSpecialCharacters(firstName) ){
           UserView.Print("First name field missing or has digits or special characters in it, please refill the field below :");
           addUser(userView.Scan(),lastName,userName,dateOfBirth);
       }
       else if(hasDigits(lastName) || hasSpecialCharacters(lastName)){
           UserView.Print("Last name field has digits or special characters  in it, please refill the field correctly below :");
           addUser(firstName,userView.Scan(),userName,dateOfBirth);
       } else if (userName.isEmpty() || userName.length() < 6 || hasSpecialCharacters(userName) ) {
           UserView.Print("User name field missing or characters less than 6 or has special characters , please refill the field correctly below :");
           addUser(firstName,lastName,userView.Scan(),dateOfBirth);
       } else if (Character.isDigit(userName.charAt(0))) {
           UserView.Print("User name field  can not start with a digit, please refill the field correctly below :");
           addUser(firstName,lastName,userView.Scan(),dateOfBirth);
       } else if (onlyDigits(userName, userName.length())) {
           UserView.Print("User name field can not contain only digits, please refill the field correctly below :");
           addUser(firstName,lastName,userView.Scan(),dateOfBirth);
       } else if(dateOfBirth.isEmpty()) {
           UserView.Print("Date of birth field missing, please refill the field correctly below with correct format:");
           addUser(firstName, lastName, userName, userView.Scan());
       }else {
            try {
                Date dateOfBirthParsed = dateFormat(dateOfBirth);
                User returnedUser = UserRegDao.returnUser(userName);
                if (returnedUser!= null) {
                    UserView.Print("User name already taken please enter new user name below : ");
                    addUser(firstName,lastName, userView.Scan(), dateOfBirth);
                } else {
                        if(dateOfBirthParsed.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)) {

                            newUser.setDateOfBirth(dateOfBirthParsed);
                            newUser.setFirstname(firstName);
                            newUser.setLastname(lastName);
                            newUser.setUsername(userName);
                            UserRegDao.saveUser(newUser);

                        }
                        else {
                            UserView.Print("Date provided is beyond current date, please enter correct date of birth bellow:");
                            addUser(firstName, lastName, userName, userView.Scan());
                        }

                    }

            }catch (Exception e){
                UserView.Print("Date provided is of incorrect format yyyy-mm-dd ,invalid month of the year or invalid day of the month, please refill the field correctly below :");
                System.out.println(e.getMessage());
                addUser(firstName,lastName,userName, userView.Scan());

            }


       }

   }
   public void returnAllUsers() throws RandomException {
       List<User> usersList = UserRegDao.returnAllUsers();
       if(usersAvailable()) {
           UserView.Print("\n <------------------- System has registered the following users -----------------> \n");
           for(User user : usersList){
               System.out.println(user);
           }
       }
       else{
           throw new RandomException("System has no registered users currently");
       }
   }

   public static void returnUserOfUserName(String userName) throws RandomException, ParseException {

        if(usersAvailable()) {
            User returnedUser = UserRegDao.returnUser(userName);
            if(returnedUser == null){
                throw new RandomException("The user is not registered in the system");
            }
            else {
                UserView userView = new UserView();
                UserView.Print("\n <==================================================================================>");
                System.out.println(returnedUser.toString());
                UserView.Print("\n Select action on user \n");
                UserView.Print("1-Attach dependants to a user");
                UserView.Print("2-Show dependants attached to a user");
                UserView.Print("3-Search for dependants by gender");
                UserView.Print("4-Search for a dependant by username,last name or first name");
                UserView.Print("5-Delete dependant by user name");
                UserView.Print("6-Update dependant by user name \n ");
                String choice = userView.Scan();
                int choice1 = Integer.parseInt(choice);
                switch (choice1){
                    case 1 :
                        DependantService.attachDependant(returnedUser);
                        break;
                    case  2:
                        DependantService.getDependantsForUser(returnedUser);
                        break;
                    case  3:
                        DependantService.getDependantsByGender(returnedUser);
                        break;
                    case  4:
                        DependantService.getDependantsByName(returnedUser);
                        break;
                    case  5:
                        DependantService.deleteDependantsByUserName(returnedUser);
                        break;
                    case  6:
                        DependantService.updateDependantByUserName(returnedUser);
                        break;
                    case  7:
                        DependantService.deleteAllDependants(returnedUser);
                        break;
                    default:
                        System.out.println("wrong choice");
                        break;
                }
            }
        }else {
            throw new RandomException("System is currently empty, no users to return");
        }
   }
   public  void updateUserOfUserName(String userName) throws ParseException, RandomException {
       UserView userView = new UserView();
        if(!usersAvailable()) {
            throw new RandomException("No users to update, system currently empty");
        }
        else {
                User returnedUser = UserRegDao.returnUser(userName);
                if (returnedUser!=null) {
                    UserView.Print("Enter users new first name (required , no digits or special characters)");
                    String firstNameNew = userView.Scan();
                    UserView.Print("Enter users new last name (optional , no digits or special characters)");
                    String lastNameNew = userView.Scan();
                    UserView.Print("Enter users new date of birth format yyyy-mm-dd (not beyond current date, right month of year, right day of month) ");
                    String dateOfBirthNew = userView.Scan();

                    //update validation

                    if(firstNameNew.isEmpty() || hasDigits(firstNameNew) || hasSpecialCharacters(firstNameNew) ){
                        UserView.Print("First name field missing, has digits or special characters  in it, please try again with valid update details :");
                        updateUserOfUserName(userName);
                    }
                    else if(hasDigits(lastNameNew) || hasSpecialCharacters(lastNameNew)){
                        UserView.Print("Last name field has digits or special characters in it, please try again with valid update details :");
                        updateUserOfUserName(userName);
                    } else if(dateOfBirthNew.isEmpty()) {
                        UserView.Print("Date of birth field missing, please try again with valid update details :");
                        updateUserOfUserName(userName);
                    }
                    else {
                        try {
                                Date dateOfBirthParsedNew = dateFormat(dateOfBirthNew);

                            if (dateOfBirthParsedNew.getYear()+1900 < Calendar.getInstance().get(Calendar.YEAR)){
                                int result = UserRegDao.updateUser(firstNameNew,lastNameNew,userName,dateOfBirthParsedNew);
                                if(result>0) {
                                    throw new RandomException("User details have been updated successfully");
                                }else {
                                    throw new RandomException("User update failed");
                                }
                            }else {
                                UserView.Print("Date provided is beyond current date, please try again with valid update details : ");
                                updateUserOfUserName(userName);
                            }

                        }catch (Exception e){
                            UserView.Print("Date provided is of incorrect format, please try again with valid update details :");
                            updateUserOfUserName(userName);
                        }
                    }
            }
                else {
                    throw  new RandomException("User not registered in the database");
                }
        }
   }
   public void deleteUserOfUserName(String userName) throws RandomException {
        if(usersAvailable()) {
            User returnedUser = UserRegDao.returnUser(userName);
                if (returnedUser!=null) {
                    int out_come = UserRegDao.deleteUser(userName);
                    UserRegDao.deleteUser(userName);
                    if(out_come >0 ) {
                        UserView.Print("User " + returnedUser.getFirstname() + " " + returnedUser.getLastname() + " has been deleted successfully");
                    }
                    else {
                        UserView.Print("Deletion operation failed");
                    }

                }
                else {
                    throw new RandomException("The username supplied is not in the database");
                }
        }
        else {
            throw new RandomException("No users in system to delete yet");
        }
   }
   public  void deleteAllUsers() throws RandomException {
       UserView userView = new UserView();

       if(usersAvailable()){
           UserView.Print("Are you sure you want to delete all users? 1-yes 0-no");
           int confirmation = Integer.parseInt(userView.Scan());
           if(confirmation==1){
               int outCome = UserRegDao.deleteAllUsers();
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
           throw  new RandomException("There are no users currently in the system");
       }
   }


}
