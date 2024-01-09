package pl.kurs.weatherforecastapp.service;

import org.apache.commons.dbcp2.BasicDataSource;
import pl.kurs.weatherforecastapp.interfaces.WhetherTheDataAreInTheDatabase;

import java.sql.*;
import java.time.LocalDate;

public class WhetherTheDataAreInTheDatabaseImpl implements WhetherTheDataAreInTheDatabase {

    public boolean checkInDatabase(String city, LocalDate date, BasicDataSource dataSource) throws SQLException {

        boolean checker = true;
        Connection connection = null;
        PreparedStatement prepStm = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            prepStm = connection.prepareStatement("SELECT * FROM forecast WHERE city = ? AND dateOf = ?;");
            prepStm.setString(1, city);
            prepStm.setDate(2, Date.valueOf(date));
            resultSet = prepStm.executeQuery();

            checker = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (prepStm != null) prepStm.close();
            if (connection != null) connection.close();
        }
        return checker;
    }
}
