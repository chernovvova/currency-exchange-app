package ru.chernov.currencyexchangeapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chernov.currencyexchangeapp.models.Currency;
import ru.chernov.currencyexchangeapp.repositories.CurrencyRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "CurrencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyRepositoryImpl currencyRepository = new CurrencyRepositoryImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().replaceFirst("/", "").toUpperCase();
        Optional<Currency> optionalCurrencyDTO = currencyRepository.findByCode(code);

        if (optionalCurrencyDTO.isPresent()) {
            new ObjectMapper().writeValue(resp.getWriter(), optionalCurrencyDTO.get());
        }
    }
}
