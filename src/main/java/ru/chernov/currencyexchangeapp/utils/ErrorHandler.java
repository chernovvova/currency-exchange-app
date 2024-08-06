package ru.chernov.currencyexchangeapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler {

    private ErrorHandler() {
    }

    public static void handleError(int code, String message, HttpServletResponse response) {
        try {
            response.setStatus(code);
            response.getWriter().println((new ObjectMapper()).createObjectNode().put("message", message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
