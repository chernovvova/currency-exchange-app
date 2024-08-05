package ru.chernov.currencyexchangeapp.utils;

public class Validator {
    public static boolean validateCurrencyCode(String currencyCode) {
        if(!currencyCode.isEmpty() && currencyCode.matches("^[A-Z]{3}$")) {
            return true;
        }

        return false;
    }
}
