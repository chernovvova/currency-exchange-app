package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Override
    public Optional<Currency> findByCode(String code) {
        final String query = "SELECT * FROM currencies WHERE code = ?";
        Optional<Currency> currencyDTOOptional = Optional.empty();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                currencyDTOOptional = Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currencyDTOOptional;
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() throws SQLException{
        final String query = "SELECT * FROM currencies";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        List<Currency> currencies = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Currency currencyDTO = getCurrency(resultSet);
            currencies.add(currencyDTO);
        }
        return currencies;
    }

    @Override
    public void save(Currency entity) {
        final String query = "INSERT INTO currencies VALUES (?, ?, ?, ?)";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);){
            preparedStatement.setString(2, entity.getCode());
            preparedStatement.setString(3, entity.getName());
            preparedStatement.setString(4, entity.getSign());

            preparedStatement.execute();

            connection.commit();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Currency entity) {

    }

    private static Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
