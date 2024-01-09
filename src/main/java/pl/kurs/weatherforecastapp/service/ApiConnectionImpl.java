package pl.kurs.weatherforecastapp.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.kurs.weatherforecastapp.interfaces.ApiConnection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiConnectionImpl implements ApiConnection {

    @Override
    public Response getForecastApiResponse(String preparedUrl) {
        Response response;
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(preparedUrl)
                    .get().build();
            response = client.newCall(request).execute();

            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
