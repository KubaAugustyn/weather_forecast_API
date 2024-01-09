package pl.kurs.service;

import org.apache.commons.dbcp2.BasicDataSource;
import pl.kurs.interfaces.PointsFromDatabase;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class PointsFromDatabaseImpl implements PointsFromDatabase {

    @Override
    public BigDecimal addUp(String city, List<LocalDate> dates, BasicDataSource dataSource) throws SQLException {
        BigDecimal totalPoints = BigDecimal.valueOf(0.0);

        Connection connection = null;
        PreparedStatement prepStm = null;
        ResultSet resultSet = null;

        for (LocalDate date : dates) {
            try {
                connection = dataSource.getConnection();
                prepStm = connection.prepareStatement("SELECT numberOfPoints FROM forecast WHERE city = ? AND dateOf = ?;");
                prepStm.setString(1, city);
                prepStm.setDate(2, Date.valueOf(date));
                resultSet = prepStm.executeQuery();

                if (resultSet.next()) {
                    totalPoints = totalPoints.add(resultSet.getBigDecimal(1));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null) resultSet.close();
                if (prepStm != null) prepStm.close();
                if (connection != null) connection.close();
            }
        }
        return totalPoints;
    }
}
