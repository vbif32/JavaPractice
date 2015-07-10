package gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Основной класс клиента, запускающий графический интерфейс
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Window_s window_s = new Window_s();
        primaryStage.setTitle("Автоматизированная система тестирования лабораторных работ");
        primaryStage.setResizable(false);
        primaryStage.setScene(window_s.main);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
