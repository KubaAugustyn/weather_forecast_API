package pl.kurs.weatherforecastapp.service;

import pl.kurs.weatherforecastapp.interfaces.UrlStringBuilder;
import pl.kurs.weatherforecastapp.constans.Constants;

public class UrlStringBuilderImpl implements UrlStringBuilder {

    @Override
    public String buildUrl(String city, int numberOfDays) {
        return Constants.BASE + Constants.FORECAST_JSON + Constants.BEGINNING_OF_KEY + Constants.API_KEY + Constants.END_OF_KEY + city + Constants.NUMBER_OF_DAYS + numberOfDays + Constants.END_OF_CALL;
    }
}
