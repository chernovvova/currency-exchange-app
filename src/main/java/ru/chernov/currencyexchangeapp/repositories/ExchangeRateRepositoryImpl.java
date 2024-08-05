package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.models.Currency;
import ru.chernov.currencyexchangeapp.models.ExchangeRate;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepositoryImpl implements ExchangeRateRepository{
    @Override
    public Optional<ExchangeRate> findById(Long id) throws SQLException {
        return Optional.empty();
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

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        while (resultSet.next()) {
            ExchangeRate exchangeRate = getExchangeRate(resultSet);
            exchangeRates.add(exchangeRate);
        }

        return exchangeRates;
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) throws SQLException {
        return null;
    }

    @Override
    public void delete(Long id) throws SQLException {

    }

    @Override
    public void update(ExchangeRate entity) throws SQLException {

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
