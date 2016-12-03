package ru.ncedu.ecomm.servlets;

import static ru.ncedu.ecomm.data.DAOFactory.getDAOFactory;
import ru.ncedu.ecomm.data.models.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private static final String INITIAL_VALUE_KEY = "initialValue";
    private static final String ROLES_KEY = "roles";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(INITIAL_VALUE_KEY, "Initial value");
        request.setAttribute(ROLES_KEY, getRoles());
        request.getRequestDispatcher("/views/pages/index.jsp").forward(request, response);
    }

    private List<Role> getRoles() {
        return getDAOFactory().getRoleDAO().getRoles();
    }
}