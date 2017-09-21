package ru.sergentum.pojo;

public class Currency {
    Float eur;
    Float usd;

    public Currency(Float eur, Float usd) {
        this.eur = eur;
        this.usd = usd;
    }

    public Float getEur() {
        return eur;
    }

    public Float getUsd() {
        return usd;
    }
}
