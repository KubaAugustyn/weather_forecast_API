package pl.kurs.service;

import pl.kurs.interfaces.UrlStringBuilder;

import static pl.kurs.constans.Constants.*;

public class UrlStringBuilderImpl implements UrlStringBuilder {

    @Override
    public String buildUrl(String city, int numberOfDays) {
        return BASE + FORECAST_JSON + BEGINNING_OF_KEY + API_KEY + END_OF_KEY + city + NUMBER_OF_DAYS + numberOfDays + END_OF_CALL;
    }
}
