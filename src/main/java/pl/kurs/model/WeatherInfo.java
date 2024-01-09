package pl.kurs.model;

import java.util.List;

public class WeatherInfo {

    private String city;
    private List<Forecast> forecasts;

    public WeatherInfo(String city, List<Forecast> forecasts) {
        this.city = city;
        this.forecasts = forecasts;
    }

    public String getCity() {
        return city;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }
}
