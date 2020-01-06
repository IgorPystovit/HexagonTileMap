package com.igorpystovit;

import com.igorpystovit.view.api.HexagonView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point of the application
 * */
@Slf4j
@ComponentScan
public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        applicationContext.getBean(HexagonView.class).launchView(primaryStage);
    }
}
