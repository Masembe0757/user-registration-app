package org.pahappa.systems.registrationapp.dao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import  org.pahappa.systems.registrationapp.models.User;
import  org.pahappa.systems.registrationapp.views.UserView;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserRegDao {
    public static void saveUser(User user){
        try {

            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.getCurrentSession();
            Transaction trs = session.beginTransaction();
            session.saveOrUpdate(user);

            trs.commit();
            UserView.Print("User has been registered successfully (*_*) ");
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to save user not created successfully");
            UserView.Print(e.getMessage());
            SessionConfiguration.shutdown();
        }
    }

    public  static List<User> returnAllUsers(){
        List<User> users = null;
            try{
                SessionFactory sf = SessionConfiguration.getSessionFactory();
                Session session = sf.openSession();
                Transaction trs = session.beginTransaction();
                Query qry = session.createQuery("from User");
                users = qry.list();
                trs.commit();
                SessionConfiguration.shutdown();

            }
            catch (Exception e){
                UserView.Print("Session to return users not created successfully");
                SessionConfiguration.shutdown();
            }
                return users;
    }
    public  static User returnUser(String userName){
        User user = null;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("from User where username = :userName");
            qry.setParameter("userName", userName);
            user = (User) qry.uniqueResult();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to return a user not created successfully");
            SessionConfiguration.shutdown();
        }
            return user;
    }
    public  static User returnUserofId(int id){
        User user = null;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("from User where id = :id");
            qry.setParameter("id", id);
            user = (User) qry.uniqueResult();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to return a user not created successfully");
            SessionConfiguration.shutdown();
        }
        return user;
    }
    public  static int deleteUser(String userName){
        int result = -1;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("delete from User where username = :userName");
            qry.setParameter("userName", userName);
            result = qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
        }catch (Exception e){
            UserView.Print("Session to delete a user not created successfully");
            SessionConfiguration.shutdown();
        }
        return result;
    }
    public  static void deleteAllUsers(){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("delete from User");
            qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to delete users not created successfully");
            SessionConfiguration.shutdown();
        }
    }
    public  static void updateUser(String firstName, String lastName, String userName, Date dateOfBirth,String password, String email){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update User set firstname =:firstName , lastname = :lastName, dateOfBirth= :dateOfBirth, email = :email, password =:password where username = :userName");
            qry.setParameter("userName", userName);
            qry.setParameter("firstName", firstName);
            qry.setParameter("lastName", lastName);
            qry.setParameter("dateOfBirth", dateOfBirth);
            qry.setParameter("email",email);
            qry.setParameter("password",password);
            qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to update a user not created successfully");
            SessionConfiguration.shutdown();
        }
    }

    public static void softDeleteUser(String userName) {
        try {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update User set deleted_at =:currentTimestamp where username = :userName");
            qry.setParameter("userName", userName);
            qry.setParameter("currentTimestamp",currentTimestamp);
            qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
        }catch (Exception e){
            UserView.Print("Session to soft delete a user not created successfully");
            SessionConfiguration.shutdown();
        }
    }
    public static void createAdmin(User user){
        try {

            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.getCurrentSession();
            Transaction trs = session.beginTransaction();
            session.saveOrUpdate(user);

            trs.commit();
            UserView.Print("Admin has been registered successfully (*_*) ");
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to save Admin not created successfully");
            UserView.Print(e.getMessage());
            SessionConfiguration.shutdown();
        }
    }

}
