package pl.kurs.weatherforecastapp.interfaces;

import pl.kurs.weatherforecastapp.model.WeatherInfo;

import java.math.BigDecimal;

public interface PointsFromWeatherInfo {
    BigDecimal addUp(WeatherInfo weatherInfo);
}
