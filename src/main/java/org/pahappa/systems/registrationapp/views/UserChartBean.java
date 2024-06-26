package org.pahappa.systems.registrationapp.views;

import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name = "userChartBean")
@SessionScoped
public class UserChartBean {
    private BarChartModel weeklyActivityModel;
    private PieChartModel genderStatisticsModel;

    @PostConstruct
    public void init() {
        createWeeklyActivityModel();
        createGenderStatisticsModel();
    }

    public BarChartModel getWeeklyActivityModel() {
        return weeklyActivityModel;
    }

    public PieChartModel getGenderStatisticsModel() {
        return genderStatisticsModel;
    }

    private static  User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    public static  int maleCount(){
        int males = 0;
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(getCurrentUser().getId());
        for(Dependant d : dependants){
            if(d.getGender().equals("male")){
                males++;
            }
        }
        return  males;
    }
    public static int femaleCount(){
        int females = 0;
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(getCurrentUser().getId());
        for(Dependant d : dependants){
            if(d.getGender().equals("female")){
                females++;
            }
        }
        return  females;
    }
    public int dependantsWithDelete(){
        int softCount = 0;
        List<Dependant> dependants = DependantDao.returnDependantsForUserId(getCurrentUser().getId());
        for(Dependant d : dependants){
            if(d.getDeleted_at()==null){
                softCount++;
            }
        }
        return softCount;

    }

    private void createWeeklyActivityModel() {
        weeklyActivityModel = new BarChartModel();

        ChartSeries users = new ChartSeries();
        users.setLabel("Users");
        users.set("Sat", 200);
        users.set("Sun", 300);
        users.set("Mon", 400);
        users.set("Tue", 500);
        users.set("Wed", 250);
        users.set("Thu", 100);
        users.set("Fri", 350);

        ChartSeries dependants = new ChartSeries();
        dependants.setLabel("Dependants");
        dependants.set("Sat", 150);
        dependants.set("Sun", 250);
        dependants.set("Mon", 350);
        dependants.set("Tue", 450);
        dependants.set("Wed", 200);
        dependants.set("Thu", 50);
        dependants.set("Fri", 300);

        weeklyActivityModel.addSeries(users);
        weeklyActivityModel.addSeries(dependants);

        weeklyActivityModel.setTitle("Weekly Activity");
        weeklyActivityModel.setLegendPosition("ne");
        Axis xAxis = weeklyActivityModel.getAxis(AxisType.X);
        xAxis.setLabel("Day");
        Axis yAxis = weeklyActivityModel.getAxis(AxisType.Y);
        yAxis.setLabel("Count");
        yAxis.setMin(0);
        yAxis.setMax(600);
    }

    private void createGenderStatisticsModel() {
        genderStatisticsModel = new PieChartModel();
        genderStatisticsModel.set("Males", maleCount());
        genderStatisticsModel.set("Females", femaleCount());
        genderStatisticsModel.setTitle("Dependant Gender Statistics");
        genderStatisticsModel.setLegendPosition("w");
        genderStatisticsModel.setFill(false);
        genderStatisticsModel.setShowDataLabels(true);
        genderStatisticsModel.setDiameter(200);
    }
}
