package pl.kurs.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;
import org.apache.commons.dbcp2.BasicDataSource;
import pl.kurs.enums.WeatherType;
import pl.kurs.exceptions.TheSelectedCityDoesNotExistException;
import pl.kurs.interfaces.ApiConnection;
import pl.kurs.interfaces.WhetherTheDataAreInTheDatabase;
import pl.kurs.interfaces.UrlStringBuilder;
import pl.kurs.interfaces.WeatherAPI;
import pl.kurs.model.Forecast;
import pl.kurs.model.WeatherInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WeatherAPIImpl implements WeatherAPI {

    private UrlStringBuilder urlStringBuilder;
    private ApiConnection apiConnection;
    private WhetherTheDataAreInTheDatabase whetherTheDataAreInTheDatabase;

    public WeatherAPIImpl(UrlStringBuilder urlStringBuilder, ApiConnection apiConnection, WhetherTheDataAreInTheDatabase whetherTheDataAreInTheDatabase) {
        this.urlStringBuilder = urlStringBuilder;
        this.apiConnection = apiConnection;
        this.whetherTheDataAreInTheDatabase = whetherTheDataAreInTheDatabase;
    }


    @Override
    public WeatherInfo getWeather(LocalDate from, LocalDate to, String city, BasicDataSource dataSource) throws IOException, SQLException {

        List<Forecast> forecastsFromTodayUntilTheEndOfTrip = new ArrayList<>();
        List<Forecast> weatherInfoForecasts = new ArrayList<>();

        int numberOfDays = (int) (ChronoUnit.DAYS.between(LocalDate.now(), to) + 1);

        List<LocalDate> selectedDates = datesFromTo(from, to);

        String preparedUrl = urlStringBuilder.buildUrl(city, numberOfDays);
        Response forecastApiResponse = apiConnection.getForecastApiResponse(preparedUrl);
        String responseAsString = forecastApiResponse.body().string();
        JsonObject jsonMain = JsonParser.parseString(responseAsString).getAsJsonObject();
        JsonArray jsonForecast;
        try {
            jsonForecast = jsonMain.getAsJsonObject("forecast")
                    .getAsJsonArray("forecastday");
        } catch (NullPointerException e) {
            throw new TheSelectedCityDoesNotExistException("We don't have weather forecast for any of your selected cities!");
        }

        for (int i = 0; i < numberOfDays; i++) {
            LocalDate date = LocalDate.parse(jsonForecast.get(i).getAsJsonObject().get("date").getAsString());
            double temperature = jsonForecast.get(i).getAsJsonObject().getAsJsonObject("day").get("avgtemp_c").getAsDouble();
            WeatherType weatherType = getWeatherTypeFromString(jsonForecast.get(i).getAsJsonObject().getAsJsonObject("day")
                    .getAsJsonObject("condition").get("text").getAsString());

            forecastsFromTodayUntilTheEndOfTrip.add(new Forecast(date, temperature, weatherType));
        }

        for (Forecast f : forecastsFromTodayUntilTheEndOfTrip) {
            if (selectedDates.contains(f.getDate())) {
                weatherInfoForecasts.add(f);
            }
            if (!whetherTheDataAreInTheDatabase.checkInDatabase(city, f.getDate(), dataSource)) {
                enterTheDataIntoTheDatabase(city, f.getDate(), f.getPoints(), dataSource);
            }
        }
        return new WeatherInfo(city, weatherInfoForecasts);
    }


    private List<LocalDate> datesFromTo(LocalDate from, LocalDate to) {
        List<LocalDate> dates = new ArrayList<>();
        while (!from.isAfter(to)) {
            dates.add(from);
            from = from.plusDays(1);
        }
        return dates;
    }

    private WeatherType getWeatherTypeFromString(String s) {
        WeatherType weatherType;
        if (s.equals("Sunny")) {
            weatherType = WeatherType.SUNNY;
        } else if (s.equals("Party cloudy") || s.equals("Cloudy")) {
            weatherType = WeatherType.CLOUDS;
        } else {
            weatherType = WeatherType.RAINY;
        }
        return weatherType;
    }

    private void enterTheDataIntoTheDatabase(String city, LocalDate date, BigDecimal points, BasicDataSource dataSource) throws SQLException {

        Connection connection = null;
        PreparedStatement prepStm = null;

        try {
            connection = dataSource.getConnection();
            prepStm = connection.prepareStatement("INSERT INTO forecast VALUES (null, ?, ?, ?);");
            prepStm.setString(1, city);
            prepStm.setDate(2, Date.valueOf(date));
            prepStm.setBigDecimal(3, points);
            prepStm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (prepStm != null) prepStm.close();
            if (connection != null) connection.close();
        }
    }

}

