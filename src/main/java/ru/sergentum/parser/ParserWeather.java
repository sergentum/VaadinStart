package ru.sergentum.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.sergentum.pojo.Forecast;

public class ParserWeather {
    public Forecast getForecast(String city){
        Forecast result = null;
        String tempToday = "";
        String tempTomor = "";
        try{
            String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&mode=xml&units=metric&appid=4f030c557b9b75512c5b086e8c155bc8";
            Document doc = Jsoup.connect(url).get();
            int findToworrow = 0;
            for (Element e : doc.select("time")) {
                findToworrow++;
                if (findToworrow == 1) {
                    Elements temper = e.select("temperature");
                    tempToday = temper.attr("value");
                } else if (findToworrow == 8) {
                    Elements temper = e.select("temperature");
                    tempTomor = temper.attr("value");
                }
            }

            result = new Forecast("", "",tempToday, tempTomor);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

}
