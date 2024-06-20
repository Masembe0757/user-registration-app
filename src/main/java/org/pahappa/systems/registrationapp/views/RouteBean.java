package org.pahappa.systems.registrationapp.views;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "routebean")
public class RouteBean implements Serializable {
    private String selectedPage;
    public  RouteBean(){}

    public String getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(String selectedPage) {
        this.selectedPage = selectedPage;
    }
    public void navigateToSelectedPage() {
        if (selectedPage != null && !selectedPage.isEmpty()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(selectedPage);
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception as needed
            }
        }
    }
}
