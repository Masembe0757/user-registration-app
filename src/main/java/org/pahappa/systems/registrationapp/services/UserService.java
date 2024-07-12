package org.pahappa.systems.registrationapp.services;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import java.util.Base64;
import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.dao.UserRegDao;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class UserService {


    //initialising a singleton
    private static UserService userService = new UserService();
    private UserService(){};
    public static UserService getUserService() {
        return userService;
    }


    private void EmailSender(String recipientEmail, String userName, String generatedPassword, String firstName, String lastName){
        // Sender's email credentials
        String senderEmail = "sendyj886@gmail.com";
        String senderPassword = "tolh dxyx hann prke";
        String subject = "Login Credentials";

        // Email body
        String emailBody = "Dear "+firstName+" "+lastName+",\n\nYour login credentials for the user Management system are:\nUsername: " + userName + "\nPassword: " + generatedPassword;

        // Configure the SMTP properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
        properties.put("mail.smtp.port", "587"); // Replace with your SMTP port
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // Create a Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender's email address
            message.setFrom(new InternetAddress(senderEmail));

            // Set recipient's email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set email subject
            message.setSubject(subject);

            // Set email body
            message.setText(emailBody);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    private  boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
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
    //Generic method to check if name provided has digits in it
    private boolean hasDigits(String str){
        boolean hasDigits = false;
        for(int i =0 ; i < str.length(); i++){
            if(Character.isDigit(str.charAt(i))){
                hasDigits =  true;
            }
        }
        return hasDigits;
    }

    //Generic method to check if there are users in the database
    private  boolean usersAvailable(){
        List<User> usersList = userService.returnAllUsers();

        return !usersList.isEmpty();
    }
   public String addUser(String firstName, String lastName, String userName, Date dateOfBirth,String password1,String password2, String email) {
        String error_message= "";

       if(firstName.isEmpty() || UserService.getUserService().hasDigits(firstName) || UserService.getUserService().hasSpecialCharacters(firstName) ){
           error_message = "First name field  has digits or special characters in it";
       }
       else if(UserService.getUserService().hasDigits(lastName) || UserService.getUserService().hasSpecialCharacters(lastName)){
           error_message = "Last name field has digits or special characters  in it";
       } else if ( userName.length() < 6 || UserService.getUserService().hasSpecialCharacters(userName) ) {
           error_message = "User name field has special characters less than 6 or has special characters";

       } else if (Character.isDigit(userName.charAt(0))) {
           error_message = "User name field  can not start with a digit";

       } else if (UserService.getUserService().onlyDigits(userName, userName.length())) {
           error_message = "User name field can not contain only digits";
       } else  if(!UserService.getUserService().isValidEmail(email)){
           error_message = "Email provided is of incorrect format";
       }else if (!password1.equals(password2)) {
           error_message = "Passwords do not match";
       } else {
                User returnedUser = UserRegDao.getUserRegDao().returnUser(userName);
                if (returnedUser!= null) {
                    error_message ="User name already taken please enter new user name";
                } else {
                    if(!UserService.getUserService().isValidPassword(password1)){
                        error_message = "Password must have more than 8 lowercase, uppercase, digits and special characters ";
                    }else {
                        if (dateOfBirth.getYear() + 1900 < Calendar.getInstance().get(Calendar.YEAR)) {
                            //Sending email with credentials
                            UserService.getUserService().EmailSender(email, userName, password1,firstName,lastName);
                            // Encrypting the password with BCrypt
                            String encodedPassword = Base64.getEncoder().encodeToString(password1.getBytes());

                            User user = new User();
                            user.setFirstname(firstName);
                            user.setLastname(lastName);
                            user.setUsername(userName);
                            user.setDateOfBirth(dateOfBirth);
                            user.setPassword(encodedPassword);
                            user.setEmail(email);
                            UserRegDao.getUserRegDao().saveUser(user);
                        } else {
                            error_message = "Date provided is beyond current date, please enter correct date of birth bellow:";
                        }
                    }

                    }
       }
       return  error_message;

   }
   public List<User> returnAllUsers()  {
       List<User> usersList = UserRegDao.getUserRegDao().returnAllUsers();
           return usersList;
       }
   public  User returnUserOfUserName(String userName)  {
       return UserRegDao.getUserRegDao().returnUser(userName);
   }
   public String updateUserOfUserName(String firstName, String lastName, String userName, Date dateOfBirth,String password1,String password2, String email) {
       String error_message = "";
        if(!UserService.getUserService().usersAvailable()) {
            error_message = "No users to update, system currently empty";
        }
        else {
                User returnedUser = UserRegDao.getUserRegDao().returnUser(userName);
                if (returnedUser!=null) {
                    //update validation

                    if( UserService.getUserService().hasDigits(firstName) || UserService.getUserService().hasSpecialCharacters(firstName) ){
                        error_message ="First name field has digits or special characters  in it";

                    }
                    else if(UserService.getUserService().hasDigits(lastName) || UserService.getUserService().hasSpecialCharacters(lastName)){
                        error_message ="Last name field has digits or special characters in it";

                    }else  if(!UserService.getUserService().isValidEmail(email)){
                        error_message = "Email provided is of incorrect format";
                    } else if (!password1.equals(password2)) {
                        error_message = "Passwords do not match";
                    } else {
                        if (!UserService.getUserService().isValidPassword(password1)) {
                            error_message = "Password must have more than 8 lowercase, uppercase, digits and special characters";
                        } else {

                            if (dateOfBirth.getYear() + 1900 < Calendar.getInstance().get(Calendar.YEAR)) {
                                //Sending email with credentials
                                UserService.getUserService().EmailSender(email, userName, password1,firstName,lastName);
                                // Encrypting the password with BCrypt
                                String encodedPassword = Base64.getEncoder().encodeToString(password1.getBytes());
                                UserRegDao.getUserRegDao().updateUser(firstName, lastName, userName, dateOfBirth, encodedPassword, email);
                            } else {
                                error_message = "Date provided is beyond current date";
                            }
                        }
                    }
                    }
                else {
                    error_message = "User not registered in the database";
                }
        }
        return  error_message;
   }
   public   String deleteUserOfUserName(String userName){
        String error_message= "";
        if(usersAvailable()) {
            User returnedUser = UserRegDao.getUserRegDao().returnUser(userName);
                if (returnedUser!=null) {

                    //Deleting user
                    UserRegDao.getUserRegDao().deleteUser(userName);

                    //Deleting dependants attached to user
                    List<Dependant> dependants =  DependantService.getDependantService().getDependantsForUser(returnedUser.getId());
                    if(!dependants.isEmpty()){
                        DependantService.getDependantService().deleteAllDependantsForUser(returnedUser.getId());
                    }
                }
                else {
                   error_message = "The username supplied is not in the database";
                }
        } else {
            error_message = "No users in system to delete yet";
        }
        return  error_message;
   }
   public  List<User> returnUserOfName(String name){
       List<User> allUsers = UserRegDao.getUserRegDao().returnAllUsers();
       List<User> returnedUsers = new ArrayList<>();
       for(User u : allUsers){
           if(u.getUsername().toLowerCase().contains(name.toLowerCase())){
               returnedUsers.add(u);
           } else if (u.getFirstname().toLowerCase().contains(name.toLowerCase())) {
               returnedUsers.add(u);
           } else if (u.getLastname().toLowerCase().contains(name.toLowerCase())) {
               returnedUsers.add(u);
           }
       }
       for(User u : returnedUsers){
           String decodedPassword = new String(Base64.getDecoder().decode(u.getPassword()));
           u.setPassword(decodedPassword);
       }
       return returnedUsers;
   }

    public  String softDeleteUserOfUserName(String userName) {
        String error_message= "";
        if(usersAvailable()) {
            User returnedUser = UserRegDao.getUserRegDao().returnUser(userName);
            if (returnedUser!=null) {
                UserRegDao.getUserRegDao().softDeleteUser(userName);
                List<Dependant> dependants = DependantService.getDependantService().getDependantsForUser(returnedUser.getId());
                if(!dependants.isEmpty()){
                    for(Dependant dependant : dependants) {
                        DependantService.getDependantService().softDeleteDependantsByUserName(dependant.getUsername());
                    }
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

    public String deleteAllUsers()  {
        String error_message ="";
       if(usersAvailable()) {
           //Deleting all users
           UserRegDao.getUserRegDao().deleteAllUsers();
           //Deleting all dependants
           List<Dependant> dependants = DependantDao.getDependantDao().returnDependants();
           if(!dependants.isEmpty()){
               DependantService.getDependantService().deleteAllDependants();
           }

       }else {
           error_message = "There are no users currently in the system";
       }
       return error_message;
   }


    public  void makAdmin(String userName) {
        UserRegDao.getUserRegDao().makeAdmin(userName);
    }
}
