package ru.sergentum.pojo;

public class Forecast {
    String name;
    String country;
    String tempToday;
    String tempTomorrow;

    public Forecast(String name, String country, String tempToday, String tempTomorrow) {
        this.name = name;
        this.country = country;
        this.tempToday = tempToday;
        this.tempTomorrow = tempTomorrow;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getTempToday() {
        return tempToday;
    }

    public String getTempTomorrow() {
        return tempTomorrow;
    }
}
