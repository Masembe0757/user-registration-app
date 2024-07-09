package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.Dependant;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DependantDao {

    //initialising a singleton
    private static DependantDao dependantDao = new DependantDao();
    private DependantDao(){};
    public static DependantDao getDependantDao() {
        return dependantDao;
    }

    public  void saveDependant(Dependant dep){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            session.save(dep);
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            System.out.println(e);
            SessionConfiguration.shutdown();
        }
    }
    public  Dependant returnDependant(String userName){
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
            SessionConfiguration.shutdown();
        }
        return dependant;
    }
    public   List<Dependant> returnDependantsForUserId(int userId){
        List<Dependant> dependants = new ArrayList<>();
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
            SessionConfiguration.shutdown();
        }
        return dependants;
    }
    public   void deleteDependant(String userName){
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
            SessionConfiguration.shutdown();
        }
    }
    public  void updateDependant(String firstName, String lastName, String userName, Date dateOfBirth, Dependant.Gender gender){
        try {
            SessionFactory sf = SessionConfiguration.getSessionFactory();
            Session session = sf.openSession();
            Transaction trs = session.beginTransaction();
            Query qry = session.createQuery("update Dependant set firstname =:firstName , lastname = :lastName, dateOfBirth= :dateOfBirth ,gender = :gender, deleted_at = null where username = :userName ");
            qry.setParameter("userName", userName);
            qry.setParameter("firstName", firstName);
            qry.setParameter("lastName", lastName);
            qry.setParameter("dateOfBirth", dateOfBirth);
            qry.setParameter("gender", gender);
            qry.executeUpdate();
            System.out.println("Dependant updated successfully in Dao");
            trs.commit();
            SessionConfiguration.shutdown();
        }
        catch (Exception e){
            e.printStackTrace();
            SessionConfiguration.shutdown();
        }
    }

    public  int deleteDependants() {
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
            SessionConfiguration.shutdown();
        }
        return result;
    }

    public  List<Dependant> returnDependants() {
        List<Dependant> dependants = new ArrayList<>();
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
            SessionConfiguration.shutdown();
        }
        return dependants;
    }

    public  void softDeleteDependant(String userName) {
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
            SessionConfiguration.shutdown();
        }
    }
}
