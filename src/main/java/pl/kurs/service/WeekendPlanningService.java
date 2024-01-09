package pl.kurs.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.json.simple.parser.ParseException;
import pl.kurs.exceptions.TheSelectedDateIsInThePastException;
import pl.kurs.exceptions.TheSelectedDateIsTooFarInTheFutureException;
import pl.kurs.interfaces.PointsFromDatabase;
import pl.kurs.interfaces.PointsFromWeatherInfo;
import pl.kurs.interfaces.WhetherTheDataAreInTheDatabase;
import pl.kurs.interfaces.WeatherAPI;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekendPlanningService {

    private WhetherTheDataAreInTheDatabase whetherTheDataAreInTheDatabase;
    private WeatherAPI weatherAPI;
    private PointsFromDatabase pointsFromDatabase;
    private PointsFromWeatherInfo pointsFromWeatherInfo;

    public WeekendPlanningService(WhetherTheDataAreInTheDatabase whetherTheDataAreInTheDatabase, WeatherAPI weatherAPI, PointsFromDatabase pointsFromDatabase, PointsFromWeatherInfo addUpThePointFromWeatherInfo) {
        this.whetherTheDataAreInTheDatabase = whetherTheDataAreInTheDatabase;
        this.weatherAPI = weatherAPI;
        this.pointsFromDatabase = pointsFromDatabase;
        this.pointsFromWeatherInfo = addUpThePointFromWeatherInfo;
    }

    public String whereShouldIGo(LocalDate from, LocalDate to, List<String> cities, BasicDataSource dataSource) throws SQLException, IOException, ParseException {
        if (from.isBefore(LocalDate.now())) {
            throw new TheSelectedDateIsInThePastException("You can't check the forecast earlier than today!");
        }
        if (to.isAfter(LocalDate.now().plusDays(9))) {
            throw new TheSelectedDateIsTooFarInTheFutureException("We have forecast for 10 days ahead!");
        }

        Map<String, BigDecimal> cityRanking = new HashMap<>();
        List<LocalDate> selectedDates = datesFromTo(from, to);

        for (String city : cities) {
            if (whetherTheDataAreInTheDatabase.checkInDatabase(city, to, dataSource)) {
                cityRanking.put(city, pointsFromDatabase.addUp(city, selectedDates, dataSource));
            } else {
                cityRanking.put(city, pointsFromWeatherInfo.addUp(weatherAPI.getWeather(from, to, city, dataSource)));
            }
        }

        String recommendedCity = null;
        BigDecimal maxValue = BigDecimal.valueOf(0.0);

        for (Map.Entry<String, BigDecimal> entry : cityRanking.entrySet()) {
            if (entry.getValue().compareTo(maxValue) >= 0.0) {
                maxValue = entry.getValue();
                recommendedCity = entry.getKey();
            }
        }
        return recommendedCity;
    }

    private List<LocalDate> datesFromTo(LocalDate from, LocalDate to) {
        List<LocalDate> dates = new ArrayList<>();
        while (!from.isAfter(to)) {
            dates.add(from);
            from = from.plusDays(1);
        }
        return dates;
    }
}
