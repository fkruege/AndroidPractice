package com.crappyweather.app.model;

public class CrappyWeather {

    String name;

    Main main;

    public Main getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public static class Main {
        double temp;

        public double getTemp() {
            return temp;
        }
    }
}
