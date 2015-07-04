package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.concurrent.TimeUnit;


/**
 * Created by Jay on 02.07.2015.
 * Класс с изображениями окон
 */
public class Window_s {

    protected Scene main;
    protected VBox visibleField;
    boolean check=false;
    int page=1; //номер окна. 1-вход, 2-регистрация,3-для студентов,4-для перподавателей
    User user = new User();

    public Window_s() {
        VBoxBuilder vboxBld = VBoxBuilder.create();
        visibleField = vboxBld.build();
        SceneBuilder scnBld = SceneBuilder.create();
        scnBld.fill(Paint.valueOf("grey"));
        scnBld.stylesheets(Main.class.getResource("window.css").toExternalForm());
        scnBld.height(550);
        scnBld.width(850);
        Window(0);
        scnBld.root(visibleField);
        scnBld.onMouseClicked(new MouseEvents(this));
        visibleField.layout();
        main = scnBld.build();
    }

    public void Window (int code) //смена окон
    {
        System.out.println(page + " page");
        if (page==1)
        {
            First(code);
        }
        else if (page==2)
        {
            Registration(code);
        }
        else if (page==3)
        {
            ForStudents();
        }
    }

    public void First (int code) //Окно входа, code - код для ошибки. 0-нет, 1 - неверное введены данные, 2 - нет соединения с интернетом, 3 - недопустимые символы
    {
        visibleField.getChildren().clear();
        //Логин
        HBoxBuilder hBoxBuilder = HBoxBuilder.create();
        HBox box1 = hBoxBuilder.build();
        Label login = new Label("Логин:");
        final TextField textField = new TextField();
        textField.setEditable(true);
        box1.getChildren().addAll(login, textField);
        box1.setSpacing(14);
        box1.setAlignment(Pos.CENTER);
        // Пароль
        HBox box2 = hBoxBuilder.build();
        Label password = new Label("Пароль:");
        final PasswordField passwordField = new PasswordField();
        passwordField.setEditable(true);
        box2.getChildren().addAll(password, passwordField);
        box2.setSpacing(6);
        box2.setAlignment(Pos.CENTER);
        // Кнопки
        HBox box3 = hBoxBuilder.build();
        Button enter = new Button("Вход");
        enter.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>(){
                              @Override
                              public void handle(javafx.event.ActionEvent actionEvent) {
                                  if ((textField.getText().isEmpty())||(passwordField.getText().isEmpty())) {
                                      check = false;
                                      System.out.println(check);
                                  }
                                  else {
                                      user.name = textField.getText();
                                      user.password = passwordField.getText();
                                      user.isLecturer=false;
                                      //ПРОВЕРКА, ЕСТЬ ЛИ ТАКИЕ В БД, ЕСЛИ ЕСТЬ - TRUE
                                      check = true; //false
                                      System.out.println(user.name);
                                      System.out.println(user.password);
                                  }
                              }
                          }
        );
        enter.setOnMouseClicked(new MouseEvents(this));
        Button registration = new Button("Регистрация");
        registration.setOnMouseClicked(new MouseEvents(this));
        box3.getChildren().addAll(enter, registration);
        box3.setSpacing(10);
        box3.setTranslateX(376);

        //Пустая
        HBox box = hBoxBuilder.build();
        if ((check)&&(code!=0))
        {
            page=3;
            Window(0);
        }
        else {
            if (code == 2) {
                Label label = new Label("Нет соединения с Интернетом");
                label.setStyle("-fx-font-style:italic;");
                box.getChildren().add(label);
            }
            else
            if ((!check)&&(code!=0)) {
                Label l = new Label("Пользователь не существует или неверно введены логин/пароль");
                l.setAlignment(Pos.CENTER_RIGHT);
                l.setStyle("-fx-font-style:italic;");
                box.getChildren().add(l);
            }
            else box.getChildren().add(new Label());
            box.setAlignment(Pos.CENTER);

            visibleField.getChildren().addAll(box1, box2, box, box3);
            visibleField.setAlignment(Pos.CENTER);
        }
    }

    public void Registration (int code) //Окно регистрации, code - код для ошибки. 0-нет
    // 2 - нет соединения с интернетом, 3 - успешно
    {
        visibleField.getChildren().clear();
        //Имя
        HBoxBuilder hBoxBuilder = HBoxBuilder.create();
        HBox box1 = hBoxBuilder.build();
        final Label name = new Label("Имя:");
        final TextField textField1 = new TextField();
        textField1.setPromptText("Иван");
        textField1.setEditable(true);
        box1.getChildren().addAll(name, textField1);
        box1.setSpacing(35);
        box1.setAlignment(Pos.CENTER);
        //Фамилия
        HBox box2 = hBoxBuilder.build();
        Label surname = new Label("Фамилия:");
        final TextField textField2 = new TextField();
        textField2.setPromptText("Иванов");
        textField2.setEditable(true);
        box2.getChildren().addAll(surname, textField2);
        box2.setSpacing(5);
        box2.setAlignment(Pos.CENTER);
        //Группа
        HBox box3 = hBoxBuilder.build();
        Label group = new Label("Группа:");
        final TextField textField3 = new TextField();
        textField3.setPromptText("ИИИ-1-11");
        textField3.setEditable(true);
        box3.getChildren().addAll(group, textField3);
        box3.setSpacing(19);
        box3.setAlignment(Pos.CENTER);
        //Логин
        HBox box4 = hBoxBuilder.build();
        Label login = new Label("Логин:");
        final TextField textField = new TextField();
        textField.setEditable(true);
        box4.getChildren().addAll(login, textField);
        box4.setSpacing(24);
        box4.setAlignment(Pos.CENTER);
        // Пароль
        HBox box5 = hBoxBuilder.build();
        final Label password = new Label("Пароль:");
        final PasswordField passwordField = new PasswordField();
        passwordField.setEditable(true);
        box5.getChildren().addAll(password, passwordField);
        box5.setSpacing(16);
        box5.setAlignment(Pos.CENTER);
        // Кнопки
        HBox box6 = hBoxBuilder.build();
        Button registration = new Button("Зарегистрироваться");
        registration.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                                     @Override
                                     public void handle(javafx.event.ActionEvent actionEvent) {
                                         if ((textField.getText().isEmpty()) || (textField1.getText().isEmpty()) || (textField2.getText().isEmpty()) || (textField3.getText().isEmpty()) || (passwordField.getText().isEmpty())) {
                                             check=false;
                                         }
                                         else {
                                             check=true;
                                             user.name = textField1.getText();
                                             user.surname = textField2.getText();
                                             user.group = textField3.getText();
                                             user.login = textField.getText();
                                             user.password = passwordField.getText();
                                             user.isLecturer=false;
                                             System.out.println(user.name);
                                             System.out.println(user.surname);
                                             System.out.println(user.group);
                                             System.out.println(user.login);
                                             System.out.println(user.password);
                                         }
                                     }
                                 }
        );
        registration.setOnMouseClicked(new MouseEvents(this));
        Button cancel = new Button("Отмена");
        cancel.setOnMouseClicked(new MouseEvents(this));
        box6.getChildren().addAll(registration, cancel);
        box6.setAlignment(Pos.CENTER);
        box6.setSpacing(10);
        visibleField.getChildren().addAll(box1, box2, box3, box4, box5);
        if ((code==3)&&(check))
        {
            System.out.println("Успех");
            Label l =new Label("Регистрация прошла успешно");
            l.setStyle("-fx-font-style:italic;");
            visibleField.getChildren().add(l);
            page=1;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Window(0);
        }
        else {
            if (code == 2) {
                Label l =new Label("Нет соединения с Интернетом");
                l.setStyle("-fx-font-style:italic;");
                visibleField.getChildren().add(l);
            }

            else if ((!check)&&(code!=0)) {
                Label l =new Label("Неверно введены данные или пользователь уже существует");
                l.setStyle("-fx-font-style:italic;");
                visibleField.getChildren().add(l);
            }
            else
            {
                visibleField.getChildren().add(new Label());
            }
            visibleField.getChildren().add(box6);
            visibleField.setAlignment(Pos.CENTER);
        }
    }

    public void ForStudents ()
    {
        System.out.println("Students");
        visibleField.getChildren().clear();
        VBoxBuilder vboxBld = VBoxBuilder.create();
        VBox vbox = vboxBld.build();
        VBox vbox1 = vboxBld.build();

        GridPane pane = new GridPane();
        HBox hBox = new HBox(); // текст
        HBox hBox1 = new HBox(); // пустая строка
        Label pysto1 = new Label("\n");
        hBox1.getChildren().add(pysto1);
        Label test = new Label("Результат работы:");
        test.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(test);
        hBox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hBox, hBox1);

        TextArea textArea = new TextArea();
        textArea.setMinSize(500,300);
        textArea.setMaxSize(500, 300);
        textArea.setEditable(true);
        vbox.getChildren().add(textArea);


        Label testResult = new Label("Тест прошел успешно");
        Label pysto2 = new Label("\n");
        testResult.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-min-width: 180px; -fx-border-color: black; -fx-border: 3px;");
        testResult.setAlignment(Pos.CENTER);
        vbox1.getChildren().addAll(testResult, pysto2);
        HBox hBox2 = new HBox(36); // cеместр
        HBox hBox3 = new HBox(); //лаба
        Label term = new Label("Семестр № ");
        Label pysto = new Label("\n");
        Label lab = new Label("Лабораторная № ");
        ComboBox comboBox = new ComboBox();
        ComboBox comboBox1 = new ComboBox();
        comboBox.setPrefSize(7,10);
        comboBox1.setPrefSize(7,10);
        hBox2.getChildren().addAll(term, comboBox);
        hBox3.getChildren().addAll(lab, comboBox1);

        vbox1.getChildren().addAll(hBox2, pysto, hBox3);


        pane.setHgap(70);
        pane.addColumn(0,vbox);
        pane.addColumn(1, vbox1);
        pane.setGridLinesVisible(true);
        pane.setMinSize(850,550);
        pane.setAlignment(Pos.TOP_CENTER);
        visibleField.getChildren().add(pane);
    }
}
