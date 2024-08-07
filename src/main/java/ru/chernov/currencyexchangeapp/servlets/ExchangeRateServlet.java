package ru.chernov.currencyexchangeapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chernov.currencyexchangeapp.models.ExchangeRate;
import ru.chernov.currencyexchangeapp.repositories.ExchangeRateRepository;
import ru.chernov.currencyexchangeapp.repositories.ExchangeRateRepositoryImpl;
import ru.chernov.currencyexchangeapp.utils.ErrorHandler;
import ru.chernov.currencyexchangeapp.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "ExchangeRateServlet", urlPatterns = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private static final ExchangeRateRepository  exchangeRateRepository = new ExchangeRateRepositoryImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("PATCH")) {
            doPatch(req, resp);
        }
        else {
            super.service(req, resp);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codePair = req.getPathInfo().replaceFirst("/", "");
        try {
            if(Validator.validateExchangeRate(codePair)) {
                String baseCode = codePair.substring(0, 3);
                String targetCode = codePair.substring(3, codePair.length());
                Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCodePair(baseCode, targetCode);

                if(exchangeRate.isPresent()) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    new ObjectMapper().writeValue(resp.getWriter(), exchangeRate.get());
                }
                else {
                    ErrorHandler.handleError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found", resp);
                }
            }
            else {
                ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Invalid code pair", resp);
            }
        } catch (SQLException e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
        } catch (Exception e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codePair = req.getPathInfo().replaceFirst("/", "");
        String rateStr = req.getParameter("rate");

        if(rateStr == null || rateStr.isEmpty()) {
            ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", resp);
        }
        else {
            BigDecimal rate = new BigDecimal(rateStr);
            try {
                if(Validator.validateExchangeRate(codePair)) {
                    String baseCode = codePair.substring(0, 3);
                    String targetCode = codePair.substring(3, codePair.length());
                    Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCodePair(baseCode, targetCode);
                    ExchangeRate newExchangeRate = null;
                    if(exchangeRate.isPresent()) {
                        exchangeRate.get().setRate(rate);
                        newExchangeRate = exchangeRateRepository.update(exchangeRate.get());
                        new ObjectMapper().writeValue(resp.getWriter(), exchangeRate.get());
                    }
                    else {
                        ErrorHandler.handleError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found", resp);
                    }
                }
                else {
                    ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Invalid code pair", resp);
                }
            } catch (SQLException e) {
                ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
            } catch (Exception e) {
                ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
                e.printStackTrace();
            }
        }
    }
}
