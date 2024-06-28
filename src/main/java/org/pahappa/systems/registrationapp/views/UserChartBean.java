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
import javax.faces.event.ComponentSystemEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ManagedBean(name = "userChartBean")
@SessionScoped
public class UserChartBean {
    int mon_dep,tue_dep,wed_dep,thur_dep,fri_dep,sat_dep,sun_dep  =0;




    private BarChartModel weeklyActivityModel;
    private PieChartModel genderStatisticsModel;


    private static User getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (User) externalContext.getSessionMap().get("currentUser");
    }

    @PostConstruct
    public void init() {

        createWeeklyActivityModel();
        createGenderStatisticsModel();
    }
    public void updateCharts() {
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

            }else{
                softCount++;
            }
        }
        return softCount;

    }
    public void createWeeklyActivityModel() {
        List<Dependant> dependantsReturned = DependantDao.returnDependantsForUserId(getCurrentUser().getId());
        if(!dependantsReturned.isEmpty()) {
            for (Dependant dependant : dependantsReturned) {
                LocalDate localDate = dependant.getCreated_at().toLocalDateTime().toLocalDate();
                LocalDate currentDateTime = LocalDate.now();
                LocalDate sevenDaysAgo = currentDateTime.minus(7, ChronoUnit.DAYS);

                //to check week
                if (!localDate.isAfter(currentDateTime) && !localDate.isBefore(sevenDaysAgo)) {
                    if (localDate.getDayOfWeek().getValue() == 1) {
                        mon_dep++;
                    } else if (localDate.getDayOfWeek().getValue() == 2) {
                        tue_dep++;
                    } else if (localDate.getDayOfWeek().getValue() == 3) {
                        wed_dep++;
                    } else if (localDate.getDayOfWeek().getValue()== 4) {
                        thur_dep++;
                    } else if (localDate.getDayOfWeek().getValue()== 5) {
                        fri_dep++;
                    } else if (localDate.getDayOfWeek().getValue() == 6) {
                        sat_dep++;
                    } else {
                        sun_dep++;
                    }
                }
            }
        }
        weeklyActivityModel = new BarChartModel();
        ChartSeries dependants = new ChartSeries();
        dependants.setLabel("Dependants");
        dependants.set("Mon", mon_dep);
        dependants.set("Tue", tue_dep);
        dependants.set("Wed", wed_dep);
        dependants.set("Thu", thur_dep);
        dependants.set("Fri", fri_dep);
        dependants.set("Sat", sat_dep);
        dependants.set("Sun", sun_dep);
        weeklyActivityModel.addSeries(dependants);

        weeklyActivityModel.setTitle("Weekly Activity");
        weeklyActivityModel.setLegendPosition("ne");
        Axis xAxis = weeklyActivityModel.getAxis(AxisType.X);
        xAxis.setLabel("Day");
        Axis yAxis = weeklyActivityModel.getAxis(AxisType.Y);
        yAxis.setLabel("Count");
        yAxis.setMin(0);
        yAxis.setMax(mon_dep+tue_dep+wed_dep+thur_dep+fri_dep+sat_dep+sun_dep+2);
       mon_dep=0;
       tue_dep=0;
       wed_dep=0;
       thur_dep=0;
       fri_dep=0;
       sat_dep=0;
       sun_dep=0;

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
