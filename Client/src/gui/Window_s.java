package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;


/**
 * Created by Jay on 02.07.2015.
 * Класс с изображениями окон
 */
public class Window_s {
    protected Scene main;
    VBox visibleField;
    int page=1; //номер окна. 1-вход, 2-регистрация,3-для студентов,4-для перподавателей
    boolean check;

    public Window_s() {
        VBoxBuilder vboxBld = VBoxBuilder.create();
        visibleField = vboxBld.build();
        SceneBuilder scnBld = SceneBuilder.create();
        scnBld.fill(Paint.valueOf("grey"));
        scnBld.stylesheets(Main.class.getResource("window.css").toExternalForm());
            //scnBld.height(250);
            //scnBld.width(300);
        scnBld.height(550);
        scnBld.width(850);
        Window(0);
        scnBld.root(visibleField);
        scnBld.onMouseClicked(new MouseEvents(page, this));
        visibleField.layout();
        main = scnBld.build();
    }

    public void Window (int code) //смена окон
    {
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
        TextField textField = new TextField();
        textField.setEditable(true);
        textField.setOnMouseClicked(new MouseEvents(1, this));
        box1.getChildren().addAll(login, textField);
        box1.setSpacing(14);
        box1.setAlignment(Pos.CENTER);
        // Пароль
        HBox box2 = hBoxBuilder.build();
        Label password = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();
        passwordField.setEditable(true);
        passwordField.setOnMouseClicked(new MouseEvents(1, this));
        box2.getChildren().addAll(password, passwordField);
        box2.setSpacing(6);
        box2.setAlignment(Pos.CENTER);
        //Пустая
        HBox box = hBoxBuilder.build();
        if(code==1)
        {
            Label l = new Label("Пользователь не существует или неверно введены логин/пароль");
            l.setAlignment(Pos.CENTER_RIGHT);
            l.setStyle("-fx-font-size:10px; -fx-font-style:italic;");
            box.getChildren().add(l);
        }
        else if (code==2)
            box.getChildren().add(new Label("Нет соединения с Интернетом"));
        else box.getChildren().add(new Label());
        box.setAlignment(Pos.CENTER);
        // Кнопки
        HBox box3 = hBoxBuilder.build();
        Button enter = new Button("Вход");
        enter.setOnMouseClicked(new MouseEvents(1, this));
        Button registration = new Button("Регистрация");
        registration.setOnMouseClicked(new MouseEvents(1, this));
        box3.getChildren().addAll(enter, registration);
        box3.setSpacing(10);
        //box3.setTranslateX(105);
        box3.setTranslateX(376);
        visibleField.getChildren().addAll(box1, box2, box, box3);
        visibleField.setAlignment(Pos.CENTER);
    }

    public void Registration (int code) //Окно регистрации, code - код для ошибки. 0-нет, 1 - неверное введены данные, 2 - нет соединения с интернетом, 3 - успешно, 4-чтение
    {
        visibleField.getChildren().clear();
        //Имя
        HBoxBuilder hBoxBuilder = HBoxBuilder.create();
        HBox box1 = hBoxBuilder.build();
        Label name = new Label("Имя:");
        TextField textField1 = new TextField();
        if (code!=4) {
            textField1.setText("Иван");
            textField1.setStyle("-fx-text-fill:grey; -fx-font-style:italic;");
            textField1.setEditable(true);
            textField1.setOnMouseClicked(new MouseEvents(2, this));
        }
        if (code==4)
        {
            textField1.setStyle("-fx-text-fill:black");
            String str="";
            System.out.println("while");
            textField1.selectAll();
            str=textField1.getSelectedText();
            System.out.println(str + " - str");
            textField1.setText(str);
        }

        box1.getChildren().addAll(name, textField1);
        box1.setSpacing(35);
        box1.setAlignment(Pos.CENTER);
        //Фамилия
        HBox box2 = hBoxBuilder.build();
        Label surname = new Label("Фамилия:");
        TextField textField2 = new TextField();
        textField2.setText("Иванов");
        textField2.setStyle("-fx-text-fill:grey; -fx-font-style:italic;");
        textField2.setEditable(true);
        textField2.setOnMouseClicked(new MouseEvents(2, this));
        box2.getChildren().addAll(surname, textField2);
        box2.setSpacing(5);
        box2.setAlignment(Pos.CENTER);
        //Группа
        HBox box3 = hBoxBuilder.build();
        Label group = new Label("Группа:");
        TextField textField3 = new TextField();
        textField3.setText("ИИИ-1-11");
        textField3.setStyle("-fx-text-fill:grey; -fx-font-style:italic;");
        textField3.setEditable(true);
        textField3.setOnMouseClicked(new MouseEvents(2, this));
        box3.getChildren().addAll(group, textField3);
        box3.setSpacing(19);
        box3.setAlignment(Pos.CENTER);
        //Логин
        HBox box4 = hBoxBuilder.build();
        Label login = new Label("Логин:");
        TextField textField = new TextField();
        textField.setEditable(true);
        textField.setOnMouseClicked(new MouseEvents(2, this));
        box4.getChildren().addAll(login, textField);
        box4.setSpacing(24);
        box4.setAlignment(Pos.CENTER);
        // Пароль
        HBox box5 = hBoxBuilder.build();
        Label password = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();
        passwordField.setEditable(true);
        passwordField.setOnMouseClicked(new MouseEvents(2, this));
        box5.getChildren().addAll(password, passwordField);
        box5.setSpacing(16);
        box5.setAlignment(Pos.CENTER);
        // Кнопки
        HBox box6 = hBoxBuilder.build();
        Button registration = new Button("Зарегистрироваться");
        registration.setOnMouseClicked(new MouseEvents(2, this));
        Button cancel = new Button("Отмена");
        cancel.setOnMouseClicked(new MouseEvents(2, this));
        box6.getChildren().addAll(registration, cancel);
        box6.setAlignment(Pos.CENTER);
        box6.setSpacing(10);
        //Пустая
        HBox box = hBoxBuilder.build();
        if(code==1)
            box.getChildren().add(new Label("Неправильный ввод данных"));
        else if (code==2)
            box.getChildren().add(new Label("Нет соединения с Интернетом"));
        else if (code==3){
            System.out.println("Успех");
            box.getChildren().add(new Label("Регистрация прошла успешно"));}
        else
        {
            System.out.println("Nothing");
            box.getChildren().add(new Label());}
        box.setAlignment(Pos.CENTER);
        visibleField.getChildren().addAll(box1, box2, box3, box4, box5, box, box6);
        visibleField.setAlignment(Pos.CENTER);
    }

    public void ForStudents ()
    {
        System.out.println("Students");
        visibleField.getChildren().clear();
        VBoxBuilder vboxBld = VBoxBuilder.create();
        VBox vbox = vboxBld.build();
        VBox vbox1 = vboxBld.build();

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        HBox hBox = new HBox();
        HBox hBox1 = new HBox();
        Label test = new Label("Результат работы:");
        test.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(test);
        hBox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hBox1, hBox);

        TextArea textArea = new TextArea();
        textArea.setMaxSize(250,250);
        textArea.setEditable(true);
        vbox.getChildren().add(textArea);

        Label testResult = new Label("Тест прошел успешно");
        testResult.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-min-width: 150px; -fx-border-color: black; -fx-border: 3px;");
        testResult.setAlignment(Pos.CENTER);
        vbox1.getChildren().add(testResult);
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();
        Label lab = new Label("Лабораторная № ");
        Label semestr = new Label("Семестр № ");
        hBox2.getChildren().add(semestr);
        hBox3.getChildren().add(lab);
        vbox1.getChildren().addAll(hBox2, hBox3);


        pane.setHgap(50);
        pane.add(vbox,0,0);
        pane.add(vbox1,1,0);

        visibleField.getChildren().add(pane);






      /*  VBoxBuilder vboxBld = VBoxBuilder.create();
        vboxBld.maxWidth(250);
        //левый стоблец
        VBox vbox1 = vboxBld.build();
        vbox1.setMaxSize(400,100);
        vbox1.setAlignment(Pos.CENTER_LEFT);
        //правый столбец
        VBox vbox2 = vboxBld.build();
        vbox2.setMaxSize(400,100);
        vbox2.setAlignment(Pos.CENTER_RIGHT);
        HBoxBuilder hboxBld = HBoxBuilder.create();
        //Верх
        HBox box1 = hboxBld.build();
        HBox box2 = hboxBld.build();
        HBox box3 = hboxBld.build();

        //Верхняя строка
        Label test = new Label("Результат работы:");
        box1.getChildren().add(test);
        box1.setAlignment(Pos.TOP_CENTER);
        //Окошко вывода данных
        TextArea textArea = new TextArea();
        textArea.setMaxSize(250,250);
        textArea.setEditable(true);
        box2.getChildren().add(textArea);

        Label testResult = new Label("Тест прошел успешно");
        testResult.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-min-width: 150px; -fx-border-color: black; -fx-border: 3px;");
        testResult.setAlignment(Pos.CENTER);
        box3.getChildren().add(testResult);


        vbox1.getChildren().addAll(box1, box2);
        vbox1.setAlignment(Pos.CENTER_LEFT);
        vbox2.getChildren().add(box3);
        vbox2.setAlignment(Pos.CENTER_RIGHT);
        visibleField.getChildren().addAll(vbox1, vbox2);*/
    }

}
