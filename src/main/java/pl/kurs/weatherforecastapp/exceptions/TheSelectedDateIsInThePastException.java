package pl.kurs.weatherforecastapp.exceptions;

public class TheSelectedDateIsInThePastException extends RuntimeException {
    public TheSelectedDateIsInThePastException() {
    }

    public TheSelectedDateIsInThePastException(String message) {
        super(message);
    }
}
