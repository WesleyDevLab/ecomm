package ru.ncedu.ecomm.data.accessobjects.impl;

import ru.ncedu.ecomm.data.accessobjects.PropertyDAO;
import ru.ncedu.ecomm.data.models.Property;
import ru.ncedu.ecomm.data.models.builders.PropertyBuilder;
import ru.ncedu.ecomm.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PostgresPropertyDAO implements PropertyDAO {

    @Override
    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(
                    "SELECT\n" +
                            "  property_id,\n" +
                            "  value\n" +
                            "FROM properties");
            while (resultSet.next()) {
                Property property = new PropertyBuilder()
                        .setPropertyId(resultSet.getString("property_id"))
                        .setValue(resultSet.getString("value"))
                        .build();

                properties.add(property);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    @Override
    public Property getPropertyById(String id) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT\n" +
                             "  property_id,\n" +
                             "  value\n" +
                             "FROM properties\n" +
                             "WHERE property_id = ?")) {
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new PropertyBuilder()
                        .setPropertyId(resultSet.getString("property_id"))
                        .setValue(resultSet.getString("value"))
                        .build();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Property addProperty(Property property) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO properties (\n" +
                             "  value,\n" +
                             "  property_id)\n" +
                             "VALUES (?, ?)")) {

            statement.setString(1, property.getValue());
            statement.setString(2, property.getId());
            statement.execute();

            return property;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteProperty(Property property) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM properties\n" +
                             "WHERE property_id = ?")) {

            statement.setString(1, property.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Property updateProperty(Property property) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE properties\n" +
                             "SET value = ?\n" +
                             "WHERE property_id = ?")) {
            statement.setString(1, property.getValue());
            statement.setString(2, property.getId());
            statement.execute();

           return property;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
