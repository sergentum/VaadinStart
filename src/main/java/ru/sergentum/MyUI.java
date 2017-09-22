package ru.sergentum;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import ru.sergentum.parser.ParserCurrency;
import ru.sergentum.parser.ParserWeather;
import ru.sergentum.pojo.Currency;
import ru.sergentum.pojo.Forecast;

import java.time.LocalDateTime;

@Theme("mytheme")
public class MyUI extends UI {

    int counter = 0;
    String city = "";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout verticalLayoutWeather = new VerticalLayout();
        VerticalLayout verticalLayoutCurrency = new VerticalLayout();
        VerticalLayout verticalLayoutCounter = new VerticalLayout();

        HorizontalLayout footerLayout = new HorizontalLayout();

//        footerLayout.

        VerticalLayout mainLayout = new VerticalLayout();

        Label mainHeader = new Label("Тестовое сетевое приложение");
        Label currentTime = new Label("Информация по состоянию на " + LocalDateTime.now().toString().replace("T", " "));
        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        Label yourIP = new Label("Ваш IP: " + webBrowser.getAddress());

        footerLayout.addComponents(currentTime, yourIP);
//        footerLayout.setComponentAlignment(currentTime, Alignment.BOTTOM_LEFT);
//        footerLayout.setComponentAlignment(yourIP, Alignment.BOTTOM_RIGHT);


        horizontalLayout.addComponents(verticalLayoutWeather, verticalLayoutCurrency, verticalLayoutCounter);


        //---------------------------------

//        System.out.println(localDateTime.toString().replace("T", " "));



        mainLayout.addComponents(mainHeader, horizontalLayout, footerLayout);
//        mainLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER); //не сработало почему то

        mainLayout.setComponentAlignment(mainHeader, Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(footerLayout, Alignment.TOP_CENTER);

        ///////////////////////////////////////////////////////////////////////
        // Create a selection component with some items
        ComboBox<String> selectCity = new ComboBox<>("Выберите город:");
        selectCity.setItems("Москва", "Новосибирск", "Санкт-Петербург");
        selectCity.setEmptySelectionAllowed(false);
        selectCity.setSelectedItem("Москва");
        city = "Moscow";


        // Handle selection event
        selectCity.addSelectionListener(
                event ->{
                    String selected = event.getSelectedItem().orElse("");

                    switch (selected){
                        case "Москва": {
                            city = "Moscow";
                            break;
                        }
                        case "Новосибирск" : {
                            city = "Novosibirsk";
                            break;
                        }

                        case "Санкт-Петербург" : {
                            city = "Saint-Petersburg";
                            break;
                        }
                        default: city = "";
                    }
                }

        );



        Label headerWeather = new Label("Погода");
//        final TextField textField1 = new TextField("Выберите город:");
        Label weatherInfoToday = new Label("");
        Label weatherInfoTomorow = new Label("");
        Button button1 = new Button("Обновить");
        Button.ClickListener listener1 = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

//  это пока что сильно круто для меня
//        button.addClickListener( e -> {
//            layout.addComponent(new Label("Thanks " + name.getValue()
//                    + ", it works!"));
//        });

                if (!city.equals("")){
                    //-----------------------------------------
                    ParserWeather parser = new ParserWeather();
                    Forecast forecast = parser.getForecast(city);
                    if (forecast != null) {
                        weatherInfoToday.setValue("Температура сегодня: " + forecast.getTempToday());
                        weatherInfoTomorow.setValue("Температура завтра: " + forecast.getTempTomorrow());
                    } else System.out.println("forecast is null");

                } else {
                    System.out.println("City is not selected");
                }

            }
        };
        button1.addClickListener(listener1);
        verticalLayoutWeather.addComponents(headerWeather, selectCity, button1, weatherInfoToday, weatherInfoTomorow);
        verticalLayoutWeather.setComponentAlignment(headerWeather, Alignment.TOP_CENTER);
        //emulate click on load page
        listener1.buttonClick( new Button.ClickEvent( button1 ) );

        ///////////////////////////////////////////////////////////////////////////////
        Label headerCurrency = new Label("Курсы валют");
        Label eurLabel = new Label("");
        Label usdLabel = new Label("");
        Button button2 = new Button("Обновить");
        Button.ClickListener listener2 = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //-------------------------------
                Currency currency = new ParserCurrency().getCurrency();
                if (currency != null){
                    eurLabel.setValue("usd: " + currency.getUsd());
                    usdLabel.setValue("eur: " + currency.getEur());
                } else System.out.println("currency is null");
            }
        };
        button2.addClickListener(listener2);
        verticalLayoutCurrency.addComponents(headerCurrency, eurLabel, usdLabel, button2);
        verticalLayoutCurrency.setComponentAlignment(headerCurrency, Alignment.TOP_CENTER);

        //emulate click on load page
        listener2.buttonClick( new Button.ClickEvent( button2 ) );


        //////////////////////////////////////////////////////////////////////////////
        Label headerCounter = new Label("Счетчик посещений");
        Label counter = new Label("" + this.counter);

        verticalLayoutCounter.addComponents(headerCounter, counter);
        verticalLayoutCounter.setComponentAlignment(headerCounter, Alignment.TOP_CENTER);


        ////////////////////////////////////////////////////////////////////////////////


        setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
