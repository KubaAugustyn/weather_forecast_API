package pl.kurs.exceptions;

public class TheSelectedCityDoesNotExistException extends RuntimeException {
    public TheSelectedCityDoesNotExistException() {
    }

    public TheSelectedCityDoesNotExistException(String message) {
        super(message);
    }
}
