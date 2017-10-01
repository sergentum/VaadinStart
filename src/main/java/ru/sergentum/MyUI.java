package ru.sergentum;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.*;

import org.bson.Document;
import ru.sergentum.parser.ParserCurrency;
import ru.sergentum.parser.ParserWeather;
import ru.sergentum.pojo.Currency;
import ru.sergentum.pojo.Forecast;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Theme("mytheme")
public class MyUI extends UI {

    static int counter = 0;
    String city = "";

    static MongoDBConnection mongoDBConnection;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout verticalLayoutWeather = new VerticalLayout();
        VerticalLayout verticalLayoutCurrency = new VerticalLayout();
        VerticalLayout verticalLayoutCounter = new VerticalLayout();

        HorizontalLayout footerLayout = new HorizontalLayout();

        VerticalLayout mainLayout = new VerticalLayout();

        Label mainHeader = new Label("Тестовое сетевое приложение");
        Label currentTime = new Label("Информация по состоянию на " + LocalDateTime.now().toString().replace("T", " "));
        WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        Label yourIP = new Label("Ваш IP: " + webBrowser.getAddress());

        footerLayout.addComponents(currentTime, yourIP);

        horizontalLayout.addComponents(verticalLayoutWeather, verticalLayoutCurrency, verticalLayoutCounter);

        //---------------------------------

        mainLayout.addComponents(mainHeader, horizontalLayout, footerLayout);

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
    public static class MyUIServlet extends VaadinServlet
            implements SessionInitListener, SessionDestroyListener {


            @Override
            protected void servletInitialized() throws ServletException {
                super.servletInitialized();
                getService().addSessionInitListener(this);
                getService().addSessionDestroyListener(this);

                mongoDBConnection = MongoDBConnection.getInstance();
                counter = mongoDBConnection.getCounter();
//                System.out.println("servletInitialized: " + counter);
            }

            @Override
            public void sessionInit(SessionInitEvent event)
            throws ServiceException {
                // Do session start stuff here
//                System.out.println("sessionInit: " + counter);
                counter++;
                mongoDBConnection.setCounter(counter);
            }

            @Override
            public void sessionDestroy(SessionDestroyEvent event) {
                // Do session end stuff here
            }
    }
}
