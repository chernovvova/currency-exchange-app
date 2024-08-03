package ru.chernov.currencyexchangeapp.repositories;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Override
    public Optional<Currency> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Currency save(Currency entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Currency entity) {

    }
}
