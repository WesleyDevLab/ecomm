package ru.ncedu.ecomm.servlets;

import ru.ncedu.ecomm.data.models.Characteristic;
import ru.ncedu.ecomm.data.models.PriceRangeModel;
import ru.ncedu.ecomm.data.models.Product;
import ru.ncedu.ecomm.data.models.builders.PriceRangeModelBuilder;
import ru.ncedu.ecomm.servlets.models.CategoryViewModel;
import ru.ncedu.ecomm.servlets.models.FilterViewModel;
import ru.ncedu.ecomm.servlets.models.ProductViewModel;
import ru.ncedu.ecomm.servlets.models.builders.CategoryViewBuilder;
import ru.ncedu.ecomm.servlets.models.builders.FilterViewModelBuilder;
import ru.ncedu.ecomm.servlets.services.ProductViewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static ru.ncedu.ecomm.data.DAOFactory.getDAOFactory;

@WebServlet(name = "FilteringServlet", urlPatterns = {"/filtering"})
public class FilteringServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long categoryId = getCategoryParentId(request);

        List<CategoryViewModel> categoryViewModels = new ArrayList<>();
        List<ProductViewModel> products = getProducts(request);
        categoryViewModels.add(new CategoryViewBuilder()
                .setId(products.isEmpty() ? null : categoryId)
                .setName(products.isEmpty() ? "No products that meet the selected criteria" : "Filtered")
                .setProducts(products)
                .build());
        request.getSession().setAttribute("price", getPriceRange(request));
        request.setAttribute("categoriesForView", categoryViewModels);
        request.getRequestDispatcher("/views/pages/category.jsp").forward(request, response);
    }

    private long getCategoryParentId(HttpServletRequest request) {
        long categoryId = Long.parseLong(request.getParameter("category_id"));
        return getDAOFactory().getCategoryDAO().getCategoriesByHierarchy(categoryId).get(0).getCategoryId();
    }

    private PriceRangeModel getPriceRange(HttpServletRequest request) {

        PriceRangeModelBuilder priceRange = new PriceRangeModelBuilder();
        long categoryId = getCategoryParentId(request);

        if (!request.getParameter("min").isEmpty()) {
            priceRange.setMin(Double.parseDouble(request.getParameter("min")));
        } else
            priceRange.setMin(getDAOFactory().getProductDAO().getProductsPriceRangeByCategoryId(categoryId).getMin());

        if (!request.getParameter("max").isEmpty()) {
            priceRange.setMax(Double.parseDouble(request.getParameter("max")));
        } else
            priceRange.setMax(getDAOFactory().getProductDAO().getProductsPriceRangeByCategoryId(categoryId).getMax());

        return priceRange.build();
    }

    private List<ProductViewModel> getProducts(HttpServletRequest request) {

        List<Characteristic> characteristics = getDAOFactory()
                .getChracteristicDAO()
                .getFilterableCharacteristicsByCategoryId(getCategoryParentId(request));
        List<FilterViewModel> filters = new ArrayList<>();

        for (Characteristic characteristic : characteristics) {
            String name = characteristic.getCharacteristicName();
            String[] params = request.getParameterValues(name);
            if (params != null) {
                filters.add(new FilterViewModelBuilder()
                        .setId(characteristic.getCharacteristicId())
                        .setName(name)
                        .setValues(Arrays.asList(params))
                        .build());
            }
        }

        List<Product> products = getDAOFactory()
                .getProductDAO()
                .getFilteredProducts(filters, getPriceRange(request), getCategoryParentId(request));

        return ProductViewService.getInstance().getProductsToView(products);
    }
}
