package ru.chernov.currencyexchangeapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chernov.currencyexchangeapp.models.Currency;
import ru.chernov.currencyexchangeapp.repositories.CurrencyRepositoryImpl;
import ru.chernov.currencyexchangeapp.utils.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CurrenciesServlet", urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    CurrencyRepositoryImpl currencyRepository = new CurrencyRepositoryImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            new ObjectMapper().writeValue(resp.getWriter(), currencyRepository.findAll());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch(SQLException e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database Error", resp);
        } catch (Exception e) {
            ErrorHandler.handleError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if(name == null || code == null || sign == null){
            ErrorHandler.handleError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", resp);
        }
        else {
            Currency currency = new Currency(name, code, sign);
            try {
                currencyRepository.save(currency);
            } catch (SQLException e) {

            }
        }
    }
}
