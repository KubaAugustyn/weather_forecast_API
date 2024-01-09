package pl.kurs.interfaces;

import pl.kurs.model.WeatherInfo;

import java.math.BigDecimal;

public interface PointsFromWeatherInfo {
    BigDecimal addUp(WeatherInfo weatherInfo);
}
