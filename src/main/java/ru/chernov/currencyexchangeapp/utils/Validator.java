package ru.chernov.currencyexchangeapp.utils;

public class Validator {

    private Validator() {
    }

    public static boolean validateCurrencyCode(String currencyCode) {
        return !currencyCode.isEmpty() && currencyCode.matches("^[A-Z]{3}$");
    }

    public static boolean validateExchangeRate(String currencyCodePair) {
        return !currencyCodePair.isEmpty() && currencyCodePair.matches("^[A-Z]{6}$");
    }
}
