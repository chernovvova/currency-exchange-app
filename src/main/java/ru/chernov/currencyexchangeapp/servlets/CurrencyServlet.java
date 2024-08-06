package ru.chernov.currencyexchangeapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chernov.currencyexchangeapp.models.Currency;
import ru.chernov.currencyexchangeapp.repositories.CurrencyRepositoryImpl;
import ru.chernov.currencyexchangeapp.utils.ErrorHandler;
import ru.chernov.currencyexchangeapp.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "CurrencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private static final CurrencyRepositoryImpl currencyRepository = new CurrencyRepositoryImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        try {
            if(Validator.validateCurrencyCode(code)) {
                Optional<Currency> optionalCurrencyDTO = currencyRepository.findByCode(code);
                if (optionalCurrencyDTO.isPresent()) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    new ObjectMapper().writeValue(resp.getWriter(), optionalCurrencyDTO.get());
                }
                else {
                    ErrorHandler.handleError(HttpServletResponse.SC_NOT_FOUND, "Currency not found", resp);
                }
            }
            else {
                ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code", resp);
            }
        } catch (SQLException e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
        } catch (Exception e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
        }
    }
}
