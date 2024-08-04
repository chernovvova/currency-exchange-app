package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.dto.CurrencyDTO;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<CurrencyDTO> {
    Optional<CurrencyDTO> findByCode(String code);
}
