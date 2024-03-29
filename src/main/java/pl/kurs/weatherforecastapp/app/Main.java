package pl.kurs.weatherforecastapp.app;

import org.apache.commons.dbcp2.BasicDataSource;
import org.json.simple.parser.ParseException;
import pl.kurs.weatherforecastapp.interfaces.ApiConnection;
import pl.kurs.weatherforecastapp.interfaces.PointsFromDatabase;
import pl.kurs.weatherforecastapp.interfaces.PointsFromWeatherInfo;
import pl.kurs.weatherforecastapp.interfaces.UrlStringBuilder;
import pl.kurs.weatherforecastapp.interfaces.WeatherAPI;
import pl.kurs.weatherforecastapp.interfaces.WhetherTheDataAreInTheDatabase;
import pl.kurs.weatherforecastapp.service.ApiConnectionImpl;
import pl.kurs.weatherforecastapp.service.PointsFromDatabaseImpl;
import pl.kurs.weatherforecastapp.service.PointsFromWeatherInfoImpl;
import pl.kurs.weatherforecastapp.service.UrlStringBuilderImpl;
import pl.kurs.weatherforecastapp.service.WeatherAPIImpl;
import pl.kurs.weatherforecastapp.service.WeekendPlanningService;
import pl.kurs.weatherforecastapp.service.WhetherTheDataAreInTheDatabaseImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException, SQLException, ParseException {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/weather_api?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        UrlStringBuilder urlStringBuilder = new UrlStringBuilderImpl();
        ApiConnection apiConnection = new ApiConnectionImpl();
        WhetherTheDataAreInTheDatabase whetherTheDataAreInTheDatabase = new WhetherTheDataAreInTheDatabaseImpl();
        WeatherAPI weatherAPI = new WeatherAPIImpl(urlStringBuilder, apiConnection, whetherTheDataAreInTheDatabase);
        PointsFromDatabase pointsFromDatabase = new PointsFromDatabaseImpl();
        PointsFromWeatherInfo pointsFromWeatherInfo = new PointsFromWeatherInfoImpl();

        WeekendPlanningService weekendPlanningService = new WeekendPlanningService(whetherTheDataAreInTheDatabase, weatherAPI, pointsFromDatabase, pointsFromWeatherInfo);

        List<String> cities = List.of("Bialystok", "Zgierz", "Radom");
        System.out.println(weekendPlanningService.whereShouldIGo(LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 5), cities, dataSource));

    }
}
