package ru.ncedu.ecomm.data.accessobjects.impl;

import ru.ncedu.ecomm.data.accessobjects.CharacteristicDAO;
import ru.ncedu.ecomm.data.models.Characteristic;
import ru.ncedu.ecomm.data.models.builders.CharacteristicBuilder;
import ru.ncedu.ecomm.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresCharacteristicDAO implements CharacteristicDAO {

    @Override
    public List<Characteristic> getCharacteristic() {
        List<Characteristic> characteristics = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT characteristic_id, " +
                            "category_id," +
                            "name," +
                            "characteristic_group_id " +
                            "FROM public.characteristics"
            );
            while (resultSet.next()) {
                Characteristic characteristic = new CharacteristicBuilder()
                        .setCharacteristicGroupId(resultSet.getLong("characteristic_group_id"))
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicName(resultSet.getString("name"))
                        .setCategoryId(resultSet.getLong("category_id"))
                        .build();

                characteristics.add(characteristic);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characteristics;
    }

    @Override
    public Characteristic addCharacteristic(Characteristic characteristic) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT  INTO public.characteristics" +
                             "(characteristic_id, " +
                             "category_id, " +
                             "name, " +
                             "characteristic_group_id) " +
                             "VALUES (?, ?, ?, ?) " +
                             "RETURNING characteristic_id"
             )) {
            statement.setLong(1, characteristic.getCharacteristicId());
            statement.setLong(2, characteristic.getCategoryId());
            statement.setString(3, characteristic.getCharacteristicName());
            statement.setLong(4, characteristic.getCharacteristicGroupId());
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                characteristic.setCharacteristicId(statement.getResultSet().getLong("characteristic_id"));
            }

            return characteristic;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Characteristic getCharacteristicById(long characteristicId) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT characteristic_id," +
                             "category_id, " +
                             "name, " +
                             "characteristic_group_id " +
                             "FROM public.characteristics " +
                             "WHERE characteristic_id = ?"
             )) {

            statement.setLong(1, characteristicId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                return new CharacteristicBuilder()
                        .setCharacteristicGroupId(resultSet.getLong("characteristic_group_id"))
                        .setCharacteristicId(resultSet.getLong("characteristic_id"))
                        .setCharacteristicName(resultSet.getString("name"))
                        .setCategoryId(resultSet.getLong("category_id"))
                        .build();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Characteristic updateCharacteristic(Characteristic characteristic) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE public.characteristics " +
                             "SET category_id = ?, " +
                             "name = ?, " +
                             "characteristic_group_id = ? " +
                             "WHERE characteristic_id = ?"
             )) {
            statement.setLong(1, characteristic.getCategoryId());
            statement.setString(2, characteristic.getCharacteristicName());
            statement.setLong(3, characteristic.getCharacteristicGroupId());
            statement.setLong(4, characteristic.getCharacteristicId());
            statement.execute();

            return characteristic;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCharacteristic(Characteristic characteristic) {

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM public.characteristics " +
                             "WHERE characteristic_id = ?"
             )) {
            statement.setLong(1, characteristic.getCharacteristicId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
