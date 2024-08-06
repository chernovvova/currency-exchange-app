package ru.chernov.currencyexchangeapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chernov.currencyexchangeapp.models.ExchangeDTO;
import ru.chernov.currencyexchangeapp.services.ExchangeService;
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

@WebServlet(name = "ExchangeServlet", urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    ExchangeService exchangeService = new ExchangeService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        String amountStr = req.getParameter("amount");

        if (baseCode == null || targetCode == null || amountStr == null
                || baseCode.isEmpty() || targetCode.isEmpty() || amountStr.isEmpty()) {
            ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", resp);
        }
        else if(!Validator.validateCurrencyCode(baseCode) && Validator.validateCurrencyCode(targetCode)){
            ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency codes", resp);
        }
        else {
            try {
                BigDecimal amount = new BigDecimal(req.getParameter("amount"));
                ExchangeDTO exchangeDTO = exchangeService.convert(baseCode, targetCode, amount);
                if(exchangeDTO == null){
                    ErrorHandler.handleError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate for convertion not found", resp);
                }
                else {
                    new ObjectMapper().writeValue(resp.getWriter(), exchangeDTO);
                }
            } catch(SQLException e) {
                e.printStackTrace();
                ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
            }
        }
    }
}
