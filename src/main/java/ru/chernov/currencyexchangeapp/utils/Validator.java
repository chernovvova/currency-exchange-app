package ru.chernov.currencyexchangeapp.utils;

public class Validator {

    private Validator() {
    }

    public static boolean validateCurrencyCode(String currencyCode) {
        return !currencyCode.isEmpty() && currencyCode.matches("^[A-Z]{3}$");
    }
}
