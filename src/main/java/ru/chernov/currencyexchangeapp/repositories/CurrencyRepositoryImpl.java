package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.dto.CurrencyDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {

    @Override
    public Optional<CurrencyDTO> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Optional<CurrencyDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<CurrencyDTO> findAll() {
        final String query = "SELECT * FROM currencies";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();
        List<CurrencyDTO> currencies = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                CurrencyDTO currencyDTO = getCurrency(resultSet);
                currencies.add(currencyDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currencies;
    }

    @Override
    public CurrencyDTO save(CurrencyDTO entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(CurrencyDTO entity) {

    }

    private static CurrencyDTO getCurrency(ResultSet resultSet) throws SQLException {
        return new CurrencyDTO(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
