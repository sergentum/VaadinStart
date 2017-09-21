package ru.sergentum.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.sergentum.pojo.Currency;
import ru.sergentum.pojo.Forecast;

public class ParserCurrency {
    public Currency getCurrency() {
        Currency result = null;
        String eur = "";
        String eurUsdRate = "";
        try{
            Document doc = Jsoup.connect("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml").get();
//            int findToworrow = 0;
            for (Element e : doc.select("Cube")) {
//                String currency = e.attr("currency");
                if (e.attr("currency").equalsIgnoreCase("rub")){
                    eur = e.attr("rate");
                } else if (e.attr("currency").equalsIgnoreCase("usd")){
                    eurUsdRate = e.attr("rate");

                }
            }
            Float eurFl =  Float.parseFloat(eur);
            Float eurUsd = Float.parseFloat(eurUsdRate);
            Float usdFl = eurFl / eurUsd;
            result = new Currency(eurFl, usdFl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
