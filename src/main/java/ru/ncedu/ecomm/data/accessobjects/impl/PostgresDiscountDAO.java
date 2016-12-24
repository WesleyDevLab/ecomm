package ru.ncedu.ecomm.data.accessobjects.impl;

import ru.ncedu.ecomm.data.accessobjects.DiscountDAO;
import ru.ncedu.ecomm.data.models.Discount;
import ru.ncedu.ecomm.utils.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresDiscountDAO implements DiscountDAO {
    @Override
    public List<Discount> getDiscount() {
        List<Discount> discounts = new ArrayList<>();

        try(Connection connection = DBUtils.getConnection();
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(
                    "SELECT discount_id, " +
                            "name, " +
                            "value " +
                            "FROM public.discount");

            while (resultSet.next()) {
                Discount discount = new Discount(resultSet);

                discounts.add(discount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return discounts;
    }
}
