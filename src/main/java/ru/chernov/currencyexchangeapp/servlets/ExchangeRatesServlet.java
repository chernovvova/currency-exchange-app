package ru.chernov.currencyexchangeapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chernov.currencyexchangeapp.models.ExchangeRate;
import ru.chernov.currencyexchangeapp.repositories.ExchangeRateRepository;
import ru.chernov.currencyexchangeapp.repositories.ExchangeRateRepositoryImpl;
import ru.chernov.currencyexchangeapp.utils.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ExchangeRatesServlet", urlPatterns = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepositoryImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();
            new ObjectMapper().writeValue(resp.getWriter(), exchangeRates);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
        }
    }
}
