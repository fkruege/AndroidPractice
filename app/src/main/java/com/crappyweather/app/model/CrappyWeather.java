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
        double pressure;
        double tempMin;
        double tempMax;

        double humidity;

        public double getHumidity() {
            return humidity;
        }

        public double getTempMax() {
            return tempMax;
        }

        public double getTempMin() {
            return tempMin;
        }

        public double getPressure() {
            return pressure;
        }

        public double getTemp() {
            return temp;
        }
    }
}
