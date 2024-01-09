package pl.kurs.weatherforecastapp.exceptions;

public class TheSelectedCityDoesNotExistException extends RuntimeException {
    public TheSelectedCityDoesNotExistException() {
    }

    public TheSelectedCityDoesNotExistException(String message) {
        super(message);
    }
}
