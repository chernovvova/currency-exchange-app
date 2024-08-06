package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.models.Currency;
import ru.chernov.currencyexchangeapp.models.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepositoryImpl implements ExchangeRateRepository{
    @Override
    public Optional<ExchangeRate> findById(Long id) throws SQLException {
        final String query = "SELECT * FROM exchange_rates WHERE id = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        Optional<ExchangeRate> exchangeRateOptional = Optional.empty();

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exchangeRateOptional = Optional.of(getExchangeRate(resultSet));
            }
        }

        return exchangeRateOptional;
    }

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        final String query = "SELECT exchange_rates.id AS id," +
                                " base.id AS base_id, base.code AS base_code, base.full_name AS base_name, base.sign AS base_sign, " +
                                " target.id AS target_id, target.code AS target_code, target.full_name AS target_name, target.sign AS target_sign, rate " +
                            "FROM exchange_rates JOIN currencies base ON base_currency_id = base.id " +
                            "JOIN currencies target ON target_currency_id = target.id";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        List<ExchangeRate> exchangeRates;
        ResultSet resultSet;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(query);
            exchangeRates = new ArrayList<>();
        }

        while (resultSet.next()) {
            ExchangeRate exchangeRate = getExchangeRate(resultSet);
            exchangeRates.add(exchangeRate);
        }

        return exchangeRates;
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) throws SQLException {
        final String query = "INSERT INTO exchange_rates VALUES (" +
                "?, " +
                "(SELECT id FROM currencies WHERE code = ?), " +
                "(SELECT id FROM currencies WHERE code = ?), " +
                "?)";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        int saveResult;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(2, entity.getBaseCurrency().getCode());
            preparedStatement.setString(3, entity.getTargetCurrency().getCode());
            preparedStatement.setBigDecimal(4, entity.getRate());

            saveResult = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        Optional<ExchangeRate> exchangeRate;

        if (saveResult > 0) {
            exchangeRate = findByCodePair(entity.getBaseCurrency().getCode(), entity.getTargetCurrency().getCode());
            if (exchangeRate.isPresent()) {
                return exchangeRate.get();
            }
        }

        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {
        final String query = "DELETE FROM exchange_rates WHERE id = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void update(ExchangeRate entity) throws SQLException {
        final String query = "UPDATE exchange_rates SET base_currency_id = ?, target_currency_id = ?, rate = ? WHERE id = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getBaseCurrency().getCode());
            preparedStatement.setString(2, entity.getTargetCurrency().getCode());
            preparedStatement.setBigDecimal(3, entity.getRate());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Optional<ExchangeRate> findByCodePair(String baseCode, String targetCode) throws SQLException {
        final String query = "SELECT exchange_rates.id AS id, " +
                "base.id AS base_id, base.code AS base_code, base.full_name AS base_name, base.sign AS base_sign, " +
                "target.id AS target_id, target.code AS target_code, target.full_name AS target_name, target.sign AS target_sign, rate " +
                "FROM exchange_rates JOIN currencies base ON base_currency_id = base.id " +
                "JOIN currencies target ON target_currency_id = target.id " +
                "WHERE base_code = ? AND target_code = ?";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        ResultSet resultSet;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        Optional<ExchangeRate> exchangeRate = Optional.empty();
        if(resultSet.next()){
            exchangeRate = Optional.of(getExchangeRate(resultSet));
        }

        return exchangeRate;
    }

    private ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setId(resultSet.getLong("id"));
        Currency base = new Currency(
                resultSet.getLong("base_id"),
                resultSet.getString("base_name"),
                resultSet.getString("base_code"),
                resultSet.getString("base_sign")
        );
        exchangeRate.setBaseCurrency(base);
        Currency target = new Currency(
                resultSet.getLong("target_id"),
                resultSet.getString("target_name"),
                resultSet.getString("target_code"),
                resultSet.getString("target_sign")
        );
        exchangeRate.setTargetCurrency(target);
        exchangeRate.setRate(resultSet.getBigDecimal("rate"));

        return exchangeRate;
    }
}
