package ru.ncedu.ecomm.servlets;

import ru.ncedu.ecomm.data.models.User;
import ru.ncedu.ecomm.data.models.builders.UserBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.ncedu.ecomm.data.DAOFactory.getDAOFactory;

@WebServlet(name = "RegistrationServlet", urlPatterns = {"/registration"})
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/pages/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(!req.getParameter("email").isEmpty()
                && !req.getParameter("password").isEmpty()
                && !req.getParameter("ConfirmPassword").isEmpty()) {
            if (checkEmail(req.getParameter("email"))) {
                if (req.getParameter("password").equals(req.getParameter("ConfirmPassword"))) {
                    if (!getDAOFactory().getUserDAO().getBoolUserByEmail(req.getParameter("email"))) {
                            User user = new UserBuilder()
                                    .setEmail(req.getParameter("email"))
                                    .setPassword(req.getParameter("password"))
                                    .setRoleId(3)
                                    .build();
                            getDAOFactory().getUserDAO().addUser(user);
                        } else {
                            req.setAttribute("answer", "Email is already in use"); //TODO: в JSP
                            req.getRequestDispatcher("/views/pages/registration.jsp").forward(req, resp);
                    }
                } else {
                    req.setAttribute("answer", "Passwords don't match"); //TODO: в JSP
                    req.getRequestDispatcher("/views/pages/registration.jsp").forward(req, resp);
                }
            } else  {
                req.setAttribute("answer", "Wrong email"); //TODO: в JSP
                req.getRequestDispatcher("/views/pages/registration.jsp").forward(req, resp);
            }
        } else  {
            req.setAttribute("answer", "Fields must not be empty"); //TODO: в JSP
            req.getRequestDispatcher("/views/pages/registration.jsp").forward(req, resp);
        }
        req.setAttribute("registration", "Registration success! Please sign in"); //TODO: в JSP
        req.getRequestDispatcher("/views/pages/login.jsp").forward(req, resp);
    }

    //TODO: можно сделать приватным
    public static boolean checkEmail(String email){
        final String check = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$"; //TODO: в константы

        Pattern patternEmailValidation = Pattern.compile(check
                                                         ,Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternEmailValidation.matcher(email);

        return matcher.find();
    }

}
