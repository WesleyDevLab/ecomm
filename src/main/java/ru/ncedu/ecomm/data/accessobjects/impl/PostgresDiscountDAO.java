package ru.ncedu.ecomm.data.accessobjects.impl;

import ru.ncedu.ecomm.data.accessobjects.DiscountDAO;
import ru.ncedu.ecomm.data.models.Discount;
import ru.ncedu.ecomm.data.models.builders.DiscountBuilder;
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
                    "SELECT\n" +
                            "  discount_id,\n" +
                            "  name,\n" +
                            "  value\n" +
                            "FROM public.discount");

            while (resultSet.next()) {
                Discount discount = new DiscountBuilder()
                        .setDiscountId(resultSet.getLong("discount_id"))
                        .setName(resultSet.getString("name"))
                        .setValue(resultSet.getInt("value"))
                        .build();

                discounts.add(discount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return discounts;
    }
}
