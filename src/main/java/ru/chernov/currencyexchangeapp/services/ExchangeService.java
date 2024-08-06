package ru.chernov.currencyexchangeapp.services;

import ru.chernov.currencyexchangeapp.models.ExchangeDTO;
import ru.chernov.currencyexchangeapp.models.ExchangeRate;
import ru.chernov.currencyexchangeapp.repositories.ExchangeRateRepository;
import ru.chernov.currencyexchangeapp.repositories.ExchangeRateRepositoryImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeService {
    private static final ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepositoryImpl();
    final int ROUNDING_NUMBER = 2;

    public ExchangeDTO convert(String baseCode, String targetCode, BigDecimal amount) throws SQLException {
        BigDecimal convertionAmount = new BigDecimal(1);
        ExchangeDTO exchangeDTO;
        try {
            Optional<ExchangeRate> forwardExchange = exchangeRateRepository.findByCodePair(baseCode, targetCode);
            if(forwardExchange.isPresent()){
                convertionAmount = forwardExchange.get().getRate().multiply(amount).setScale(ROUNDING_NUMBER, BigDecimal.ROUND_HALF_UP);

                exchangeDTO = new ExchangeDTO(
                        forwardExchange.get().getBaseCurrency(),
                        forwardExchange.get().getTargetCurrency(),
                        forwardExchange.get().getRate(),
                        amount,
                        convertionAmount
                );

                return exchangeDTO;
            }

            Optional<ExchangeRate> backwardExchange = exchangeRateRepository.findByCodePair(targetCode, baseCode);
            if(backwardExchange.isPresent()){
                BigDecimal convertionRate = new BigDecimal(1).
                        divide(backwardExchange.get().getRate(), ROUNDING_NUMBER, BigDecimal.ROUND_HALF_UP);

                convertionAmount = amount.multiply(convertionRate).
                        setScale(ROUNDING_NUMBER, BigDecimal.ROUND_HALF_UP);

                exchangeDTO = new ExchangeDTO(
                        backwardExchange.get().getTargetCurrency(),
                        backwardExchange.get().getBaseCurrency(),
                        convertionRate,
                        amount,
                        convertionAmount
                );

                return exchangeDTO;
            }

            Optional<ExchangeRate> exchangeUSDToBase = exchangeRateRepository.findByCodePair("USD", baseCode);
            Optional<ExchangeRate> exchangeUSDToTarget = exchangeRateRepository.findByCodePair("USD", targetCode);
            if(exchangeUSDToBase.isPresent() && exchangeUSDToTarget.isPresent()){
                BigDecimal convertionRate = exchangeUSDToTarget.get().getRate().
                        divide(exchangeUSDToBase.get().getRate(), ROUNDING_NUMBER, BigDecimal.ROUND_HALF_UP);

                convertionAmount = amount.multiply(convertionRate).
                        setScale(ROUNDING_NUMBER, BigDecimal.ROUND_HALF_UP);

                exchangeDTO = new ExchangeDTO(
                        exchangeUSDToBase.get().getTargetCurrency(),
                        exchangeUSDToTarget.get().getTargetCurrency(),
                        convertionRate,
                        amount,
                        convertionAmount
                );

                return exchangeDTO;
            }

            return null;
        } catch(SQLException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }
}
