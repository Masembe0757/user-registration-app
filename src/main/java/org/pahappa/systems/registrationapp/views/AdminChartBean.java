package org.pahappa.systems.registrationapp.views;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.annotation.PostConstruct;

import org.pahappa.systems.registrationapp.dao.DependantDao;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;
import org.primefaces.model.chart.*;

import java.util.Date;
import java.util.List;

@ManagedBean(name = "adminChartBean")
@SessionScoped
public class AdminChartBean {
    int mon_user,mon_dep,tue_user,tue_dep,wed_user,wed_dep,thur_user,thur_dep,fri_user,fri_dep,sat_user,sat_dep,sun_user,sun_dep  =0;
    private BarChartModel weeklyActivityModel;
    private PieChartModel genderStatisticsModel;


    @PostConstruct
    public void init() {
        List<User> users = UserService.returnAllUsers();
        List<Dependant> dependants = DependantDao.returnDependants();

        if(!users.isEmpty()) {
            for (User user : users) {
                Date date = (Date) user.getCreated_at();
                if (date.getDay() == 1) {
                    mon_user++;
                } else if (date.getDay() == 2) {
                    tue_user++;
                } else if (date.getDay() == 3) {
                    wed_user++;
                } else if (date.getDay() == 4) {
                    thur_user++;
                } else if (date.getDay() == 5) {
                    fri_user++;
                } else if (date.getDay() == 6) {
                    sat_user++;
                } else {
                    sun_user++;
                }
            }
        }

        if(!dependants.isEmpty()) {
            for (Dependant dependant : dependants) {
                Date date = (Date) dependant.getCreated_at();
                if (date.getDay() == 1) {
                    mon_dep++;
                } else if (date.getDay() == 2) {
                    tue_dep++;
                } else if (date.getDay() == 3) {
                    wed_dep++;
                } else if (date.getDay() == 4) {
                    thur_dep++;
                } else if (date.getDay() == 5) {
                    fri_dep++;
                } else if (date.getDay() == 6) {
                    sat_dep++;
                } else {
                    sun_dep++;
                }
            }
        }



        createWeeklyActivityModel();
        createGenderStatisticsModel();
    }

    public BarChartModel getWeeklyActivityModel() {
        return weeklyActivityModel;
    }

    public PieChartModel getGenderStatisticsModel() {
        return genderStatisticsModel;
    }
    public static  int maleCount(){
        int males = 0;
        List<Dependant> dependants = DependantDao.returnDependants();
        for(Dependant d : dependants){
            if(d.getGender().equals("male")){
                males++;
            }
        }
        return  males;
    }
    public static int femaleCount(){
        int females = 0;
        List<Dependant> dependants = DependantDao.returnDependants();
        for(Dependant d : dependants){
            if(d.getGender().equals("female")){
                females++;
            }
        }
        return  females;
    }

    private void createWeeklyActivityModel() {
        weeklyActivityModel = new BarChartModel();

        ChartSeries users = new ChartSeries();
        users.setLabel("Users");
        users.set("Sat", sat_user);
        users.set("Sun", sun_user);
        users.set("Mon", mon_user);
        users.set("Tue", tue_user);
        users.set("Wed", wed_user);
        users.set("Thu", thur_user);
        users.set("Fri", fri_user);

        ChartSeries dependants = new ChartSeries();
        dependants.setLabel("Dependants");
        dependants.set("Sat", sat_dep);
        dependants.set("Sun", sun_dep);
        dependants.set("Mon", mon_dep);
        dependants.set("Tue", tue_dep);
        dependants.set("Wed", wed_dep);
        dependants.set("Thu", thur_dep);
        dependants.set("Fri", fri_dep);

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
