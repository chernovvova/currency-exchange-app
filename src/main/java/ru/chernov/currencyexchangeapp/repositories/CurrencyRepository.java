package ru.chernov.currencyexchangeapp.repositories;

import java.util.Currency;
import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency> {
    Optional<Currency> findByCode(String code);
}
