package pl.kurs.service;

import pl.kurs.interfaces.PointsFromWeatherInfo;
import pl.kurs.model.Forecast;
import pl.kurs.model.WeatherInfo;

import java.math.BigDecimal;

public class PointsFromWeatherInfoImpl implements PointsFromWeatherInfo {
    @Override
    public BigDecimal addUp(WeatherInfo weatherInfo) {
        BigDecimal totalPoints = BigDecimal.valueOf(0.0);

        for (Forecast f : weatherInfo.getForecasts()) {
            totalPoints = totalPoints.add(f.getPoints());
        }

        return totalPoints;
    }
}
