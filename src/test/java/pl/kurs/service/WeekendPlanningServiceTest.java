package pl.kurs.service;


import org.apache.commons.dbcp2.BasicDataSource;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.weatherforecastapp.exceptions.TheSelectedDateIsInThePastException;
import pl.kurs.weatherforecastapp.exceptions.TheSelectedDateIsTooFarInTheFutureException;
import pl.kurs.weatherforecastapp.interfaces.PointsFromDatabase;
import pl.kurs.weatherforecastapp.interfaces.PointsFromWeatherInfo;
import pl.kurs.weatherforecastapp.interfaces.WeatherAPI;
import pl.kurs.weatherforecastapp.interfaces.WhetherTheDataAreInTheDatabase;
import pl.kurs.weatherforecastapp.model.WeatherInfo;
import pl.kurs.weatherforecastapp.service.WeekendPlanningService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

public class WeekendPlanningServiceTest {

    @Mock
    private WhetherTheDataAreInTheDatabase whetherTheDataAreInTheDatabase;
    @Mock
    private WeatherAPI weatherAPI;
    @Mock
    private PointsFromDatabase pointsFromDatabase;
    @Mock
    private PointsFromWeatherInfo pointsFromWeatherInfo;
    @Mock
    private BasicDataSource dataSource;
    @Mock
    private WeatherInfo weatherInfo;

    private WeekendPlanningService weekendPlanningService;

    private LocalDate todayDate = LocalDate.now();
    private LocalDate dateIn4Days = LocalDate.now().plusDays(4);
    private List<LocalDate> dates = datesFromTo(todayDate, dateIn4Days);
    private List<String> cities = List.of("Warszawa", "Zakopane", "Krakow");

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        weekendPlanningService = new WeekendPlanningService(whetherTheDataAreInTheDatabase, weatherAPI, pointsFromDatabase, pointsFromWeatherInfo);
    }

    @Test(expected = TheSelectedDateIsInThePastException.class)
    public void shouldThrowTheSelectedDateIsInThePastExceptionWhenDateIsInThePast() throws SQLException, IOException, ParseException {
        LocalDate dateInThePast = LocalDate.of(2023, 1, 1);
        weekendPlanningService.whereShouldIGo(dateInThePast, dateIn4Days, cities, dataSource);
    }

    @Test(expected = TheSelectedDateIsTooFarInTheFutureException.class)
    public void shouldThrowTheSelectedDateIsTooFarInTheFutureExceptionWhenDateIsMoreThan10DaysAhead() throws IOException, ParseException, SQLException {
        LocalDate dateMoreThan10DaysAhead = LocalDate.now().plusDays(10);
        weekendPlanningService.whereShouldIGo(todayDate, dateMoreThan10DaysAhead, cities, dataSource);
    }

    @Test
    public void shouldCollectDataFromDataBaseIfItIsAlreadyThere() throws SQLException, IOException, ParseException {
        for (String city : cities) {
            Mockito.when(whetherTheDataAreInTheDatabase.checkInDatabase(city, dateIn4Days, dataSource)).thenReturn(true);
            Mockito.when(pointsFromDatabase.addUp(city, dates, dataSource)).thenReturn(BigDecimal.valueOf(69));
        }
        weekendPlanningService.whereShouldIGo(todayDate, dateIn4Days, cities, dataSource);
        for (String city : cities) {
            Mockito.verify(weatherAPI, times(0)).getWeather(todayDate, dateIn4Days, city, dataSource);
            Mockito.verify(pointsFromWeatherInfo, times(0)).addUp(weatherInfo);
        }
    }

    @Test
    public void shouldCollectDataFromApiIfItIsNotInTheDatabase() throws SQLException, IOException, ParseException {
        for (String city : cities) {
            Mockito.when(whetherTheDataAreInTheDatabase.checkInDatabase(city, dateIn4Days, dataSource)).thenReturn(false);
            Mockito.when(weatherAPI.getWeather(todayDate, dateIn4Days, city, dataSource)).thenReturn(weatherInfo);
        }
        Mockito.when(pointsFromWeatherInfo.addUp(weatherInfo)).thenReturn(BigDecimal.valueOf(69));
        weekendPlanningService.whereShouldIGo(todayDate, dateIn4Days, cities, dataSource);
        for (String city : cities) {
            Mockito.verify(weatherAPI, times(1)).getWeather(todayDate, dateIn4Days, city, dataSource);
            Mockito.verify(pointsFromDatabase, times(0)).addUp(city, dates, dataSource);
        }
    }

    @Test
    public void shouldReturnZakopane() throws SQLException, IOException, ParseException {
        for (String city : cities) {
            Mockito.when(whetherTheDataAreInTheDatabase.checkInDatabase(city, dateIn4Days, dataSource)).thenReturn(true);
        }
        Mockito.when(pointsFromDatabase.addUp("Warszawa", dates, dataSource)).thenReturn(BigDecimal.valueOf(69));
        Mockito.when(pointsFromDatabase.addUp("Zakopane", dates, dataSource)).thenReturn(BigDecimal.valueOf(96));
        Mockito.when(pointsFromDatabase.addUp("Krakow", dates, dataSource)).thenReturn(BigDecimal.valueOf(69));
        String recommendedCity = weekendPlanningService.whereShouldIGo(todayDate, dateIn4Days, cities, dataSource);
        Assert.assertEquals("Zakopane", recommendedCity);
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