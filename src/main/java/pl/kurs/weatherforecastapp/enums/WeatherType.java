package pl.kurs.weatherforecastapp.enums;

public enum WeatherType {
    SUNNY(10),
    CLOUDS(5),
    RAINY(0);

    private final int scale;

    WeatherType(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }
}
