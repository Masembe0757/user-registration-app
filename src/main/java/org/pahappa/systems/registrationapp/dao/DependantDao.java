package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.views.UserView;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class DependantDao {
    public static void saveDependant(Dependant dep){
        try {

            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            session.save(dep);
            trs.commit();
            UserView.Print("Dependant has been registered on user successfully (*_*) ");
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to save dependant not created successfully");
            System.out.println(e);
            SessionConfiguration.shutdown();
        }
    }
    public  static Dependant returnDependant(String userName){
        Dependant dependant = null;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("from Dependant where username = :userName");
            qry.setParameter("userName", userName);
            dependant = (Dependant) qry.uniqueResult();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to return a dependant not created successfully");
            SessionConfiguration.shutdown();
        }
        return dependant;
    }
    public  static List<Dependant> returnDependantsForUserId(int userId){
        List<Dependant> dependants = null;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("from Dependant where user_id = :userId");
            qry.setParameter("userId", userId);
            dependants = qry.list();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to return a dependant not created successfully");
            SessionConfiguration.shutdown();
        }
        return dependants;
    }
    public  static void deleteDependant(String userName){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("delete from Dependant where username = :userName");
            qry.setParameter("userName", userName);
            qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
            System.out.println("Dependant deleted successfully");
        }catch (Exception e){
            UserView.Print("Session to delete a dependant not created successfully");
            SessionConfiguration.shutdown();
        }
    }
    public  static void updateDependant(String firstName, String lastName, String userName, Date dateOfBirth, String gender){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update Dependant set firstname =:firstName , lastname = :lastName, dateOfBirth= :dateOfBirth ,gender = :gender , deleted_at = null where username = :userName ");
            qry.setParameter("userName", userName);
            qry.setParameter("firstName", firstName);
            qry.setParameter("lastName", lastName);
            qry.setParameter("dateOfBirth", dateOfBirth);
            qry.setParameter("gender", gender);
            qry.executeUpdate();
            trs.commit();
            System.out.println("Dependant updated successfully");
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to update a dependant not created successfully");
            SessionConfiguration.shutdown();
        }
    }

    public static int deleteDependants() {
        int result = -1;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("delete from Dependant ");
            result = qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to delete dependants not created successfully");
            SessionConfiguration.shutdown();
        }
        return result;
    }

    public static List<Dependant> returnDependants() {
        List<Dependant> dependants = null;
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("from Dependant");
            dependants = qry.list();
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            UserView.Print("Session to return all dependants not created successfully");
            SessionConfiguration.shutdown();
        }
        return dependants;
    }

    public static void softDeleteDependant(String userName) {
        try {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update Dependant set deleted_at =:currentTimestamp where username = :userName");
            qry.setParameter("userName", userName);
            qry.setParameter("currentTimestamp",currentTimestamp);
            qry.executeUpdate();
            trs.commit();
            SessionConfiguration.shutdown();
            System.out.println("Dependant soft deleted successfully");
        }catch (Exception e){
            UserView.Print("Session to soft delete a dependant not created successfully");
            SessionConfiguration.shutdown();
        }
    }
}
