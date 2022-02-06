package com.sharedrive.desktopapp;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesktopAppApplication {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\dimitri\\Desktop\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver.exe");
        Application.launch(ChartApplication.class, args);
     }

}
