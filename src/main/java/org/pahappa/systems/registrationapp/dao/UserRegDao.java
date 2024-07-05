package org.pahappa.systems.registrationapp.dao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import  org.pahappa.systems.registrationapp.models.User;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserRegDao {


    //initialising a singleton
    private static UserRegDao userRegDao = new UserRegDao();
    private UserRegDao(){};
    public static UserRegDao getUserRegDao() {
        return userRegDao;
    }


    public  void saveUser(User user){
        try {

            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.getCurrentSession();
            Transaction trs = session.beginTransaction();
            session.save(user);

            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            SessionConfiguration.shutdown();
        }
    }

    public  List<User> returnAllUsers(){
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
                SessionConfiguration.shutdown();
            }
                return users;
    }

    public  List<User> returnAllUsersPaginated(int start, int size){
        List<User> users = null;
        try{
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("from User");
            qry.setFirstResult(start-1);
            qry.setMaxResults(size);
            users = qry.list();
            trs.commit();
            SessionConfiguration.shutdown();

        }
        catch (Exception e){
            SessionConfiguration.shutdown();
        }
        return users;
    }







    public   User returnUser(String userName){
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
            SessionConfiguration.shutdown();
        }
            return user;
    }
    public  User returnUserofId(int id){
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
            SessionConfiguration.shutdown();
        }
        return user;
    }
    public  int deleteUser(String userName){
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
            SessionConfiguration.shutdown();
        }
        return result;
    }
    public   void deleteAllUsers(){
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
            SessionConfiguration.shutdown();
        }
    }
    public   void updateUser(String firstName, String lastName, String userName, Date dateOfBirth,String password, String email){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update User set firstname =:firstName , lastname = :lastName, dateOfBirth= :dateOfBirth, email = :email, password =:password, deleted_at =null where username = :userName");
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
            SessionConfiguration.shutdown();
        }
    }

    public  void softDeleteUser(String userName) {
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
            SessionConfiguration.shutdown();
        }
    }
    public  void createAdmin(User user){
        try {

            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.getCurrentSession();
            Transaction trs = session.beginTransaction();
            session.saveOrUpdate(user);

            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            SessionConfiguration.shutdown();
        }
    }

    public  void makeAdmin(String userName) {
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update User set role = 1 where username = :userName");
            qry.setParameter("userName", userName);
            qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
        }catch (Exception e){
            SessionConfiguration.shutdown();
        }
    }
}
