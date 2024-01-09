package pl.kurs.weatherforecastapp.interfaces;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;
import java.time.LocalDate;

public interface WhetherTheDataAreInTheDatabase {
    boolean checkInDatabase(String city, LocalDate date, BasicDataSource dataSource) throws SQLException;
}
