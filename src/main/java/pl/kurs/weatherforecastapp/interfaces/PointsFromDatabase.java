package pl.kurs.weatherforecastapp.interfaces;

import org.apache.commons.dbcp2.BasicDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PointsFromDatabase {
    BigDecimal addUp(String city, List<LocalDate> dates, BasicDataSource dataSource) throws SQLException;
}
