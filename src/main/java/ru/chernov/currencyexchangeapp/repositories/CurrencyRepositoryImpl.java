package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Override
    public Optional<Currency> findByCode(String code) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE code = ?";
        Optional<Currency> currencyDTOOptional = Optional.empty();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            currencyDTOOptional = Optional.of(getCurrency(resultSet));
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
            Currency currency = getCurrency(resultSet);
            currencies.add(currency);
        }
        return currencies;
    }

    @Override
    public Currency save(Currency entity) throws SQLException{
        final String query = "INSERT INTO currencies VALUES (?, ?, ?, ?)";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(2, entity.getCode());
        preparedStatement.setString(3, entity.getName());
        preparedStatement.setString(4, entity.getSign());

        int saveResult = preparedStatement.executeUpdate();

        Long id = null;

        if (saveResult > 0) {
            Optional<Currency> currency = findByCode(entity.getCode());
            id = currency.get().getId();
        }

        entity.setId(id);

        return entity;
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
                resultSet.getString("full_name"),
                resultSet.getString("code"),
                resultSet.getString("sign")
        );
    }
}
