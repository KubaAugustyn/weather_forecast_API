package pl.kurs.weatherforecastapp.exceptions;

public class TheSelectedDateIsTooFarInTheFutureException extends RuntimeException {
    public TheSelectedDateIsTooFarInTheFutureException() {
    }

    public TheSelectedDateIsTooFarInTheFutureException(String message) {
        super(message);
    }
}
