package pl.kurs.interfaces;

import org.apache.commons.dbcp2.BasicDataSource;
import org.json.simple.parser.ParseException;
import pl.kurs.model.WeatherInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public interface WeatherAPI {
    WeatherInfo getWeather(LocalDate from, LocalDate to, String city, BasicDataSource dataSource) throws IOException, ParseException, SQLException;
}
