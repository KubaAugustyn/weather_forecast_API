package pl.kurs.weatherforecastapp.interfaces;

import okhttp3.Response;

public interface ApiConnection {
    Response getForecastApiResponse(String preparedUrl);
}
