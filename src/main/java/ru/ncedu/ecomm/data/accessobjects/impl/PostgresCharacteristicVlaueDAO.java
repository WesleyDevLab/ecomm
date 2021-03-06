package ru.ncedu.ecomm.data.accessobjects.impl;

import ru.ncedu.ecomm.data.accessobjects.CharacteristicValueDAO;
import ru.ncedu.ecomm.data.models.CharacteristicValue;
import ru.ncedu.ecomm.data.models.builders.CharacteristicValueBuilder;
import ru.ncedu.ecomm.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresCharacteristicVlaueDAO implements CharacteristicValueDAO {

    @Override
    public List<CharacteristicValue> getCharacteristicValue() {
        List<CharacteristicValue> characteristicValues = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT\n" +
                            "  characteristic_id,\n" +
                            "  product_id,\n" +
                            "  value\n" +
                            "FROM public.characteristic_values");
            while (resultSet.next()) {
                CharacteristicValue characteristicValue = new CharacteristicValueBuilder()
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicValue(resultSet.getString("value"))
                        .setProductId(resultSet.getLong("product_id"))
                        .build();

                characteristicValues.add(characteristicValue);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characteristicValues;
    }

    @Override
    public List<CharacteristicValue> getCharacteristicValueById(long id) {
        List<CharacteristicValue> characteristicValuesById = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT\n" +
                             "  characteristic_id,\n" +
                             "  product_id,\n" +
                             "  value\n" +
                             "FROM public.characteristic_values\n" +
                             "WHERE characteristic_id = ?")) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CharacteristicValue characteristicValue = new CharacteristicValueBuilder()
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicValue(resultSet.getString("value"))
                        .setProductId(resultSet.getLong("product_id"))
                        .build();

                characteristicValuesById.add(characteristicValue);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return characteristicValuesById;
    }

    @Override
    public List<CharacteristicValue> getCharacteristicValueByProductId(long id) {
        List<CharacteristicValue> characteristicValuesByProductId = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT\n" +
                             "  characteristic_id,\n" +
                             "  product_id,\n" +
                             "  value\n" +
                             "FROM public.characteristic_values\n" +
                             "WHERE product_id = ?")) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CharacteristicValue characteristicValue = new CharacteristicValueBuilder()
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicValue(resultSet.getString("value"))
                        .setProductId(resultSet.getLong("product_id"))
                        .build();

                characteristicValuesByProductId.add(characteristicValue);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return characteristicValuesByProductId;
    }

    @Override
    public CharacteristicValue getCharacteristicValueByIdAndProductId(long productId, long characteristicId) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT\n" +
                             "  characteristic_id,\n" +
                             "  product_id,\n" +
                             "  value\n" +
                             "FROM public.characteristic_values\n" +
                             "WHERE product_id = ?\n" +
                             "      AND characteristic_id = ?")) {
            statement.setLong(1, productId);
            statement.setLong(2, characteristicId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                       return new CharacteristicValueBuilder()
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicValue(resultSet.getString("value"))
                        .setProductId(resultSet.getLong("product_id"))
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public CharacteristicValue addCharacteristicValue(CharacteristicValue characteristicValue) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO public.characteristic_values\n" +
                             "(product_id,\n" +
                             " value,\n" +
                             " characteristic_id)\n" +
                             "VALUES (?, ?, ?)\n" +
                             "RETURNING characteristic_id")) {
            statement.setLong(1, characteristicValue.getProductId());
            statement.setString(2, characteristicValue.getCharacteristicValue());
            statement.setLong(3, characteristicValue.getCharacteristicId());
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                characteristicValue.setCharacteristicId(statement.getResultSet().getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return characteristicValue;
    }

    @Override
    public CharacteristicValue updateCharacteristicValue(CharacteristicValue characteristicValue) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE public.characteristic_values\n" +
                             "SET value = ?\n" +
                             "WHERE characteristic_id = ?\n" +
                             "      AND product_id = ?"
             )) {
            statement.setString(1, characteristicValue.getCharacteristicValue());
            statement.setLong(2, characteristicValue.getCharacteristicId());
            statement.setLong(3, characteristicValue.getProductId());
            statement.execute();

            return characteristicValue;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCharacteristicValue(CharacteristicValue characteristicValue) {
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM public.characteristic_values\n" +
                             "WHERE characteristic_id = ?\n" +
                             "      AND product_id = ?"
             )) {
            statement.setLong(1, characteristicValue.getCharacteristicId());
            statement.setLong(2, characteristicValue.getProductId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CharacteristicValue> getCharacteristicValueByCharacteristicId(long id) {
        List<CharacteristicValue> characteristics = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT DISTINCT ON (value)\n" +
                             "  characteristic_id,\n" +
                             "  product_id,\n" +
                             "  value\n" +
                             "FROM public.characteristic_values\n" +
                             "WHERE characteristic_id = ?")) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               characteristics.add(new CharacteristicValueBuilder()
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicValue(resultSet.getString("value"))
                        .setProductId(resultSet.getLong("product_id"))
                        .build());

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return characteristics;
    }

}
