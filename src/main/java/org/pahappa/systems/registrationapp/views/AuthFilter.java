package org.pahappa.systems.registrationapp.views;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/pages/protected/*") // Apply to the protected pages
public class AuthFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
        // If you have any filter initialization, put it here
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            // User is not logged in, redirect to login page
            res.sendRedirect(req.getContextPath() + "/pages/login/login.xhtml");
        } else {
            // User is logged in, proceed with the request
            chain.doFilter(request, response);
        }
    }
    public void destroy() {
        // If you have any filter cleanup, put it here
    }
}

