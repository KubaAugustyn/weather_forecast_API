package pl.kurs.weatherforecastapp.model;

import pl.kurs.weatherforecastapp.enums.WeatherType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Forecast {

    private LocalDate date;
    private double temperature;
    private WeatherType weatherType;
    private BigDecimal points;

    public Forecast(LocalDate date, double temperature, WeatherType weatherType) {
        this.date = date;
        this.temperature = temperature;
        this.weatherType = weatherType;
        setPoints();
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints() {
        this.points = BigDecimal.valueOf(temperature * weatherType.getScale());
    }

}
