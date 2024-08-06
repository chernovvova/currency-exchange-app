package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Override
    public Optional<Currency> findByCode(String code) throws SQLException {
        final String query = "SELECT id, code, full_name, sign FROM currencies WHERE code = ?";
        Optional<Currency> currency = Optional.empty();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                currency = Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return currency;
    }

    @Override
    public Optional<Currency> findById(Long id) throws SQLException {
        final String query = "SELECT id, code, full_name, sign FROM currencies WHERE id = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        Optional<Currency> currency = Optional.empty();

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                currency = Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return currency;
    }

    @Override
    public List<Currency> findAll() throws SQLException{
        final String query = "SELECT id, code, full_name, sign FROM currencies";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        List<Currency> currencies = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Currency currency = getCurrency(resultSet);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return currencies;
    }

    @Override
    public Currency save(Currency entity) throws SQLException{
        final String query = "INSERT INTO currencies VALUES (?, ?, ?, ?)";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        int saveResult;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(2, entity.getCode());
            preparedStatement.setString(3, entity.getName());
            preparedStatement.setString(4, entity.getSign());

            saveResult = preparedStatement.executeUpdate();
        }

        Long id = null;
        if (saveResult > 0) {
            Optional<Currency> currency = findByCode(entity.getCode());
            if(currency.isPresent()){
                id = currency.get().getId();
            }
        }

        entity.setId(id);

        return entity;
    }

    @Override
    public void delete(Long id) throws SQLException{
        final String query = "DELETE FROM currencies WHERE id = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Currency update(Currency entity) throws SQLException{
        final String query = "UPDATE Currencies SET code = ?, full_name = ?, sign = ? WHERE id = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSign());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return entity;
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
