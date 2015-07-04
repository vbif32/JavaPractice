package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jay on 02.07.2015.
 * Класс с описанием реакций на события
 */
public class MouseEvents implements EventHandler<MouseEvent> {

    int scene;
    Window_s window; //начальная сцена
    User user;

    public MouseEvents(Window_s window) {
        this.scene = window.page;
        this.window = window;
        this.user = window.user;
    }

    @Override
    public void handle(MouseEvent event) {
        //System.out.println(event.getSceneX());
        //System.out.println(event.getSceneY());
        //System.out.println(event.getSource().getClass().getSimpleName());
        //ОКНО ВХОДА
        if ((event.getSceneX() >= 376) && (event.getSceneX() <= 426) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene == 1)) {
            System.out.println("Enter");
            //ПРОВЕРИТЬ СОЕДИНЕНИЕ С ИНТЕРНЕТОМ
            boolean checkI = true;
            if (!checkI) //нет соединения
            {
                window.Window(2);
            } else {
                window.Window(3);
            }
        } else if ((event.getSceneX() >= 435) && (event.getSceneX() <= 532) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene == 1)) {
            //ПЕРЕХОД К ОКНУ РЕГИСТРАЦИИ
            System.out.println("Registration");
            //ПРОВЕРИТЬ СОЕДИНЕНИЕ С ИНТЕРНЕТОМ
            boolean checkI = true; //ЗАМЕНА НА ФУНКЦИЮ
            if (!checkI) //нет соединения
            {
                window.Window(2);
            } else {
                window.page = 2;
                window.Window(0);
            }
        }

        //ОКНО РЕГИСТРАЦИИ
        else if ((event.getSceneX() >= 323) && (event.getSceneX() <= 462) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene == 2)) {
            //ЗАРЕГИСТРИРОВАТЬ
            System.out.println("Do registration");
            //ПРОВЕРИТЬ СОЕДИНЕНИЕ С ИНТЕРНЕТОМ
            boolean checkI = true; //ЗАМЕНА НА ФУНКЦИЮ
            if (!checkI) //нет соединения
            {
                window.Window(2);
            } else {
                window.Window(3);
            }
        } else if ((event.getSceneX() >= 472) && (event.getSceneX() <= 784) && (event.getSource().getClass().getSimpleName().equals("Button")) && (this.scene == 2)) {
            //ВЕРНУТЬСЯ К ОКНУ ВХОДА
            System.out.println("Cancel");
            window.page = 1;
            window.Window(0);
        }
    }
}