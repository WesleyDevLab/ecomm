package ru.ncedu.ecomm.servlets;

import ru.ncedu.ecomm.data.models.Characteristic;
import ru.ncedu.ecomm.data.models.CharacteristicValue;
import ru.ncedu.ecomm.servlets.models.FilterViewModel;
import ru.ncedu.ecomm.servlets.models.builders.FilterViewModelBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.ncedu.ecomm.data.DAOFactory.getDAOFactory;

@WebServlet(name = "FilterServlet", urlPatterns = {"/filter"})
public class FilterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request);
    }

    private void process(HttpServletRequest request) {

        List<FilterViewModel> filters = new ArrayList<>();
        List<Characteristic> filterableCharacteristics = getDAOFactory()
                .getChracteristicDAO()
                .getFilterableCharacteristicsByCategoryId(getCategoryParentId(request));

        for (Characteristic characteristic : filterableCharacteristics){
             filters.add(new FilterViewModelBuilder()
                     .setName(characteristic.getCharacteristicName())
                     .setValues(getValuesByCharacteristicId(characteristic.getCharacteristicId()))
                     .build());
        }
        request.setAttribute("filters", filters);
    }

    private List<String> getValuesByCharacteristicId(long id){
        List<CharacteristicValue> characteristicValues = getDAOFactory().getCharacteristicValueDAO().getCharacteristicValueByCharacteristicId(id);
        List<String> values = new ArrayList<>();

        for (CharacteristicValue characteristicValue : characteristicValues){
            values.add(characteristicValue.getCharacteristicValue());
        }

        return values;
    }

    private long getCategoryParentId(HttpServletRequest request){
        long categoryId = Long.parseLong(request.getParameter("category_id"));
        return getDAOFactory().getCategoryDAO().getCategoriesByHierarchy(categoryId).get(0).getCategoryId();
    }
}
