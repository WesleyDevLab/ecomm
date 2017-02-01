package ru.ncedu.ecomm.servlets;

import ru.ncedu.ecomm.data.models.User;
import ru.ncedu.ecomm.servlets.services.ProfileService;
import ru.ncedu.ecomm.servlets.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showProfile(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showProfile(req, resp);
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = getUserIdFromSession(req);
        if (userId != 0) {
            ProfileService profileService = new ProfileService(userId);
            User userProfile = profileService.getUserProfile();
            initAttributesProfile(userProfile, req);
        }else {
            UserService.getInstance().redirectToLoginIfNeeded(req, resp);
        }
    }

    private long getUserIdFromSession(HttpServletRequest req){
        HttpSession authorization = req.getSession();
        if (authorization.getAttribute("userId") == null) {
            return 0;
        }
        long userIdFromSession = Long.parseLong(authorization.getAttribute("userId").toString());
        return userIdFromSession;
    }

    private void initAttributesProfile(User userProfile, HttpServletRequest req) {
        req.setAttribute("firstName", userProfile.getFirstName());
        req.setAttribute("lastName", userProfile.getLastName());
        req.setAttribute("email", userProfile.getEmail());
    }
}
