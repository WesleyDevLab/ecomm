package ru.ncedu.ecomm.servlets;

import ru.ncedu.ecomm.servlets.services.UserService;
import ru.ncedu.ecomm.servlets.services.shoppingCart.ShoppingCartIconService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddToShoppingCartServlet", urlPatterns = {"/addToShoppingCart"})
public class AddToShoppingCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/pages/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ShoppingCartIconService cartIconService = getCartIconService(req, resp);
        cartIconService.addToShoppingCart();
        resp.sendRedirect("/views/pages/cart.jsp");
    }

    private ShoppingCartIconService getCartIconService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long userId = UserService.getInstance().getCurrentUserId(req, resp);
        long productId = Long.parseLong(req.getParameter("productId"));
        return new ShoppingCartIconService(userId, productId);
    }
}