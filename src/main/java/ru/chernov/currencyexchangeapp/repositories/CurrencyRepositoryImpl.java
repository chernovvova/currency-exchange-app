package ru.chernov.currencyexchangeapp.repositories;

import ru.chernov.currencyexchangeapp.dto.CurrencyDTO;

import java.sql.*;
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

        try (Statement statement = connection.createStatement();){
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
    public void save(CurrencyDTO entity) {
        final String query = "INSERT INTO currencies VALUES (?, ?, ?, ?)";
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection connection = dataBaseConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(2, entity.getCode());
            preparedStatement.setString(3, entity.getName());
            preparedStatement.setString(4, entity.getSign());

            preparedStatement.execute();

            connection.commit();
        } catch(SQLException e) {
            e.printStackTrace();
        }
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
