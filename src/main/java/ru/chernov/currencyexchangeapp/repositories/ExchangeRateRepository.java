package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.models.ExchangeRate;

import java.sql.SQLException;
import java.util.Optional;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate> {
    Optional<ExchangeRate> findByCodePair(String baseCode, String targetCode) throws SQLException;
}
