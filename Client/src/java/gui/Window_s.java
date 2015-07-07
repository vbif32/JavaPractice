package gui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
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
    ForTest forTest = new ForTest();
    //shed.User user = new shed.User();

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
        visibleField.layout();
        main = scnBld.build();
    }

    public void Window (int code) //смена окон
    {
        System.out.println(page + " page");
        if (page==1)
        {
            first(code);
        }
        else if (page==2)
        {
            registration(code);
        }
        else if (page==3)
        {
            //  if (!user.isLecturer)
            forStudents();
            //  else
            //forTeachers();
        }
    }

    public void first(int code) //Окно входа, code - код для ошибки. 0-нет, 1 - неверное введены данные,
    // 2 - нет соединения с интернетом
    {
        visibleField.getChildren().clear();
        //Логин
        HBox forLogin = new HBox();
        Label login = new Label("Логин:");
        final TextField textForLogin = new TextField();
        textForLogin.setPromptText("не менее 3 символов");
        textForLogin.setEditable(true);
        forLogin.getChildren().addAll(login, textForLogin);
        forLogin.setSpacing(14);
        forLogin.setAlignment(Pos.CENTER);
        // Пароль
        HBox forPassword = new HBox();
        Label password = new Label("Пароль:");
        final PasswordField textForPassword = new PasswordField();
        textForPassword.setPromptText("не менее 6 символов");
        textForPassword.setEditable(true);
        forPassword.getChildren().addAll(password, textForPassword);
        forPassword.setSpacing(6);
        forPassword.setAlignment(Pos.CENTER);
        // Кнопки
        HBox forButtons = new HBox();
        Button enter = new Button("Вход");
        enter.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>(){
                              @Override
                              public void handle(javafx.event.ActionEvent actionEvent) {
                                  boolean checkI = true; //СОЕДИНЕНИЕ С ИНТЕРНЕТОМ
                                  if (!checkI) //нет соединения
                                  {
                                      Window(2);
                                  } else {

                                      if ((textForLogin.getText().isEmpty())||(textForPassword.getText().isEmpty())) {
                                          check = false;
                                      }
                                      else if((!textForLogin.getText().matches("\\w{3,}"))||(!textForPassword.getText().matches("\\w{6,}")))
                                      {
                                          check=false;
                                      }
                                      else {
                                          user.login = textForLogin.getText();
                                          user.password = textForPassword.getText();
                                          //ПРОВЕРКА, ЕСТЬ ЛИ ТАКИЕ В БД, ЕСЛИ ЕСТЬ - TRUE
                                          check = true; //false
                                      }
                                      Window(3);
                                  }
                              }
                          }
        );

        Button registration = new Button("Регистрация");
        registration.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean checkI = true; //ЗАМЕНА НА ФУНКЦИЮ
                if (!checkI) //нет соединения
                {
                    Window(2);
                } else {
                    page = 2;
                    Window(0);
                }
            }
        });
        forButtons.getChildren().addAll(enter, registration);
        forButtons.setSpacing(10);
        forButtons.setTranslateX(373);

        //Комментарии
        HBox comments = new HBox();
        if ((check)&&(code!=0))
        {
            page=3;
            Window(0);
        }
        else {
            if (code == 2) {
                Label label = new Label("Нет соединения с Интернетом");
                label.setStyle("-fx-font-style:italic;");
                comments.getChildren().add(label);
            }
            else
            if ((!check)&&(code!=0)) {
                Label l = new Label("Пользователь не существует или неверно введены логин/пароль");
                l.setAlignment(Pos.CENTER_RIGHT);
                l.setStyle("-fx-font-style:italic;");
                comments.getChildren().add(l);
            }
            else comments.getChildren().add(new Label());
            comments.setAlignment(Pos.CENTER);

            visibleField.getChildren().addAll(forLogin, forPassword, comments, forButtons);
            visibleField.setAlignment(Pos.CENTER);
        }
    }

    public void registration (int code) //Окно регистрации, code - код для ошибки. 0-нет
    // 2 - нет соединения с интернетом, 3 - успешно
    {
        visibleField.getChildren().clear();
        //Имя
        HBoxBuilder hBoxBuilder = HBoxBuilder.create();
        HBox forName = hBoxBuilder.build();
        final Label name = new Label("Имя:");
        final TextField textForName = new TextField();
        textForName.setPromptText("Иван");
        textForName.setEditable(true);
        forName.getChildren().addAll(name, textForName);
        forName.setSpacing(43);
        forName.setAlignment(Pos.CENTER);
        //Фамилия
        HBox forSurname = hBoxBuilder.build();
        Label surname = new Label("Фамилия:");
        final TextField textForSurname = new TextField();
        textForSurname.setPromptText("Иванов");
        textForSurname.setEditable(true);
        forSurname.getChildren().addAll(surname, textForSurname);
        forSurname.setSpacing(14);
        forSurname.setAlignment(Pos.CENTER);
        // Отчество
        HBox forPatronymic = hBoxBuilder.build();
        final Label patronymic = new Label("Отчество:");
        final TextField textForPatronymic = new TextField();
        textForPatronymic.setPromptText("Иванович");
        textForPatronymic.setEditable(true);
        forPatronymic.getChildren().addAll(patronymic, textForPatronymic);
        forPatronymic.setSpacing(6);
        forPatronymic.setAlignment(Pos.CENTER);
        //Группа
        HBox forGroup = hBoxBuilder.build();
        Label group = new Label("Группа:");
        final TextField textForGroup = new TextField();
        textForGroup.setPromptText("ИИИ-1-11");
        textForGroup.setEditable(true);
        forGroup.getChildren().addAll(group, textForGroup);
        forGroup.setSpacing(27);
        forGroup.setAlignment(Pos.CENTER);
        //Логин
        HBox forLogin = hBoxBuilder.build();
        Label login = new Label("Логин:");
        final TextField textForLogin = new TextField();
        textForLogin.setPromptText("не менее 3 символов");
        textForLogin.setEditable(true);
        forLogin.getChildren().addAll(login, textForLogin);
        forLogin.setSpacing(32);
        forLogin.setAlignment(Pos.CENTER);
        // Пароль
        HBox forPassword = hBoxBuilder.build();
        final Label password = new Label("Пароль:");
        final PasswordField textForPassword = new PasswordField();
        textForPassword.setPromptText("не менее 6 символов");
        textForPassword.setEditable(true);
        forPassword.getChildren().addAll(password, textForPassword);
        forPassword.setSpacing(24);
        forPassword.setAlignment(Pos.CENTER);
        // Кнопки
        HBox forButtons = hBoxBuilder.build();
        Button registration = new Button("Зарегистрироваться");
        registration.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                                     @Override
                                     public void handle(javafx.event.ActionEvent actionEvent) {
                                         boolean checkI = true; //ЗАМЕНА НА ФУНКЦИЮ
                                         if (!checkI) //нет соединения
                                         {
                                             Window(2);
                                         } else {

                                             if ((textForLogin.getText().isEmpty()) || (textForName.getText().isEmpty()) || (textForSurname.getText().isEmpty()) ||
                                                     (textForGroup.getText().isEmpty()) || (textForPassword.getText().isEmpty()) || (textForPatronymic.getText().isEmpty())) {
                                                 check = false;
                                             } else if ((!textForName.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) || (!textForSurname.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) ||
                                                     (!textForPatronymic.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) || (!textForGroup.getText().matches("([А-Я]|Ё){3}\\-\\d+\\-[1-9]{2}"))
                                                     || (!textForLogin.getText().matches("\\w{3,}")) || (!textForPassword.getText().matches("\\w{6,}"))) {
                                                 check = false;
                                             } else {
                                                 user.name = textForName.getText();
                                                 user.surname = textForSurname.getText();
                                                 user.secondName = textForPatronymic.getText();
                                                 user.group = textForGroup.getText();
                                                 user.login = textForLogin.getText();
                                                 user.password = textForPassword.getText();
                                                 user.isLecturer = false;
                                                 //!!!СРАВНИТЬ С БД!!! ЕСЛИ НЕТ ВСЕ ОКБ ИНАЧЕ CHECK=FALSE
                                                 check = true;
                                                 System.out.println(user.name);
                                                 System.out.println(user.surname);
                                                 System.out.println(user.group);
                                                 System.out.println(user.login);
                                                 System.out.println(user.password);
                                             }
                                             Window(3);
                                         }
                                     }
                                 }
        );
        Button cancel = new Button("Отмена");
        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 1;
                Window(0);
            }
        });
        forButtons.getChildren().addAll(registration, cancel);
        forButtons.setAlignment(Pos.CENTER);
        forButtons.setSpacing(10);
        visibleField.getChildren().addAll(forName, forSurname, forPatronymic, forGroup, forLogin, forPassword);
        if ((code==3)&&(check))
        {
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
            visibleField.getChildren().add(forButtons);
            visibleField.setAlignment(Pos.CENTER);
        }
    }

    public void forStudents ()
    {
        System.out.println("Students");
        visibleField.getChildren().clear();
        VBoxBuilder vboxBld = VBoxBuilder.create();
        VBox leftVBox = vboxBld.build();
        VBox rightVBox = vboxBld.build();

        rightVBox.setMinWidth(200);
        leftVBox.getChildren().addAll(new Label(user.surname + " " + user.name + " " + user.secondName, new Label("\n")));
        leftVBox.getChildren().addAll(new Label(user.group));
        rightVBox.getChildren().addAll(new Label("\n"), new Label("\n"), new Label("\n"), new Label("\n"));

        final GridPane pane = new GridPane();

        final HBox Highline = new HBox(); // текст
        Label test = new Label("Введите код в поле или загрузите соответствующий файл");
        test.setAlignment(Pos.CENTER_RIGHT);
        Highline.getChildren().add(test);
        Highline.setAlignment(Pos.CENTER);
        leftVBox.getChildren().addAll(Highline, new Label("\n"));

        final TextArea textArea = new TextArea();
        textArea.setMinSize(500,300);
        textArea.setMaxSize(500, 300);
        textArea.setEditable(true);
        leftVBox.getChildren().add(textArea);

        final Label testResult = new Label(""); //НЕ ЗАБЫТЬ ИЗМЕНЯТЬ ТЕКСТ
        testResult.setMinSize(200, 10);
        testResult.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: black; -fx-border: 3px;");
        testResult.setAlignment(Pos.CENTER);
        rightVBox.getChildren().addAll(testResult, new Label("\n"));
        HBox sub = new HBox(12); // предмет
        sub.setMaxSize(180,15);
        HBox sem = new HBox(47); // семестр
        sem.setMaxSize(180,15);
        HBox task = new HBox(10); // лаба
        task.setMaxSize(180,15);
        HBox variant = new HBox(46); // вариант
        variant.setMaxSize(180,15);

        Label subject = new Label("Предмет ");
        Label term = new Label("Семестр № ");
        Label lab = new Label("Лабораторная № ");
        Label var = new Label("Вариант № ");
        ComboBox forSubject = new ComboBox();
        final ComboBox forTerm = new ComboBox();
        ComboBox forLab = new ComboBox();
        ComboBox forVariant = new ComboBox();
        forSubject.setMaxSize(100, 18);
        forTerm.setMaxSize(50, 18);
        forLab.setMaxSize(50, 18);
        forVariant.setMaxSize(50, 18);
        forSubject.setMinSize(100, 18);
        forTerm.setMinSize(50, 18);
        forLab.setMinSize(50, 18);
        forVariant.setMinSize(50, 18);
        //!!!ЗАПОЛНИТЬ!!!
        sub.getChildren().addAll(subject, forSubject);
        sem.getChildren().addAll(term, forTerm);
        task.getChildren().addAll(lab, forLab);
        variant.getChildren().addAll(var, forVariant);
        rightVBox.getChildren().addAll(sub, new Label("\n"), sem, new Label("\n"), task, new Label("\n"), variant);
        leftVBox.getChildren().add(new Label("\n"));

        //ВЫБОР ЗАГРУЗКИ
        final HBox choose = new HBox();
        choose.setSpacing(10);
        final Button chooseCode = new Button("Проверять код");
        final Button chooseFile = new Button("Проверять файл");
        final int[] result = {0};
        final Label label = new Label("Выберите вариант проверки");
        chooseCode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                chooseCode.setText("Проверяется код");
                chooseCode.setStyle("-fx-text-decoration: underline; -fx-background-color: linear-gradient(rgba(0, 98, 184, 0.5), rgba(0, 98, 184, 0.9));" +
                        "radial-gradient(center 50% -30%, radius 200%, #0080f0 30%, #00498a 50%);");
                result[0]=1;
                label.setText("");
                if (chooseFile.getText().equals("Проверяется файл"))
                {
                    chooseFile.setText("Проверять файл");
                    chooseFile.setStyle("-fx-text-decoration: none; -fx-background-color: linear-gradient(rgba(36, 153, 255, 0.5), rgba(36, 153, 255, 0.9));" +
                            "radial-gradient(center 50% -30%, radius 200%, #57b0ff 30%, #0080f0 50%);");
                }
            }
        });

        chooseFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                chooseFile.setText("Проверяется файл");
                chooseFile.setStyle("-fx-text-decoration: underline; -fx-background-color: linear-gradient(rgba(0, 98, 184, 0.5), rgba(0, 98, 184, 0.9));" +
                        " radial-gradient(center 50% -30%, radius 200%, #0080f0 30%, #00498a 50%);");
                result[0]=2;
                label.setText("");
                if (chooseCode.getText().equals("Проверяется код"))
                {
                    chooseCode.setText("Проверять код");
                    chooseCode.setStyle("-fx-text-decoration: none; -fx-background-color: linear-gradient(rgba(36, 153, 255, 0.5), rgba(36, 153, 255, 0.9));" +
                            "radial-gradient(center 50% -30%, radius 200%, #57b0ff 30%, #0080f0 50%);");
                }
            }
        });

        choose.getChildren().addAll(chooseCode, chooseFile,label);
        //ЗАГРУЗКА ФАЙЛА С ЛАБОРАТОРНОЙ
        final HBox files = new HBox(); // для загрузки файлов
        files.setSpacing(15);
        Button openL = new Button();
        openL.setAlignment(Pos.BOTTOM_RIGHT);
        openL.setText("Загрузить лабораторную");
        final Label done = new Label("Файл загружен");
        final File[] file = new File[1];
        openL.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFileChooser laba = new JFileChooser();
                if (forTest.subject != 666) { //что-то выбрано
                    int rezult = laba.showDialog(null, "Открыть файл");
                    if (rezult == JFileChooser.APPROVE_OPTION) {
                        if (forTest.subject == 0)
                            laba.addChoosableFileFilter(new FileFilter() {
                                @Override
                                public boolean accept(File f) {
                                    if (f.isFile() || f.getName().contains(".jar"))
                                        return true;
                                    else
                                        return false;
                                }
                                @Override
                                public String getDescription() {
                                    return ".jar";
                                }
                            });
                        else if (forTest.subject == 1)
                            laba.addChoosableFileFilter(new FileFilter() {
                                @Override
                                public boolean accept(File f) {
                                    if (f.isFile() || f.getName().contains(".cpp"))
                                        return true;
                                    else
                                        return false;
                                }
                                @Override
                                public String getDescription() {
                                    return ".cpp";
                                }
                            });
                        file[0] = laba.getSelectedFile();
                        System.out.println(file[0].getAbsolutePath());
                        done.setText("Файл загружен");
                    } else done.setText("");
                } else done.setText("Выберите предмет");
                files.getChildren().add(done);
            }
        });
        files.getChildren().add(openL);
        leftVBox.getChildren().addAll(choose, new Label("\n"),files, new Label("\n"), new Label("Для компиляции cpp файлов требуется предустановленный g++ компилятор"));

        Button exit = new Button("Выйти");
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 1;
                Window(0);
            }
        });
        //ЗАГРУЗИТЬ ТЕСТ
        Button toLoadTest = new Button("Запустить тест");
        final Label mistakesInCode = new Label();
        final Label mistakesInCom = new Label();
        final Label mistakesInTerm = new Label();
        final Label mistakesInLab = new Label();
        final Label mistakesInVar = new Label();
        toLoadTest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (result[0]==0)
                    mistakesInCode.setText("Выберите вариант проверки");
                else
                {
                    if (forTest.subject == 666) //если не выбран предмет
                        mistakesInCom.setText("Выберите предмет");
                    if (forTest.term == 666) // не выбран семестр
                        mistakesInTerm.setText("Выберите семестр");
                    else mistakesInTerm.setText("");
                    if (forTest.number == 666) // не выбран номер лабы
                        mistakesInLab.setText("Выберите номер лабораторной");
                    else mistakesInLab.setText("");
                    if (forTest.variant == 666) // не выбран вариант
                        mistakesInVar.setText("Выберите вариант");
                    else mistakesInVar.setText("");
                    if ((file[0] != null) || (!textArea.getText().isEmpty())) // если есть лаба
                    {
                        if (result[0]==1) {
                            forTest.code = textArea.getText();
                            forTest.laba=null;
                        }
                        if (result[0]==2)
                        {
                            forTest.laba = file[0];
                            forTest.code = null;
                        }
                        mistakesInCode.setText("");
                    } else
                        mistakesInCode.setText("Введите код/добавьте файл");
                    //ПРОВЕРКА РЕЗУЛЬТАТА
                    final boolean res = false;
                    if (res)
                        testResult.setText("Тест прошел успешно");
                    else testResult.setText("Тест не пройден");
                }
            }
        });



        rightVBox.getChildren().addAll(new Label("\n"), toLoadTest);
        rightVBox.getChildren().addAll(new Label("\n"), mistakesInCode, mistakesInCom, mistakesInTerm, mistakesInLab, mistakesInVar, new Label("\n"));
        rightVBox.getChildren().addAll(new Label("\n"), new Label("\n"), new Label("\n"), new Label("\n"),new Label("\n"));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        exit.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(exit);
        rightVBox.getChildren().add(hBox);

        pane.setHgap(50);
        pane.addColumn(0, leftVBox);
        pane.addColumn(1, rightVBox);
        pane.setMinSize(850,550);
        pane.setAlignment(Pos.CENTER);
        visibleField.getChildren().add(pane);
    }

    public void forTeachers() {
        VBox main = new VBox();
        //ТАБЛИЧКА
        TableView firstTable = new TableView();
        firstTable.setMaxSize(800, 400);
        firstTable.setMinSize(800, 400);
        firstTable.setEditable(true);

        TableColumn fio = new TableColumn();
        firstTable.getColumns().setAll(fio);
        fio.setText("ФИО");
        fio.setMinWidth(150);
        fio.setMaxWidth(150);

        //ПАНЕЛЬКИ
        TabPane tabPane = new TabPane();

        Tab java = new Tab("Программирование");
        java.setClosable(false);

        Tab cpp = new Tab("АиСД");
        cpp.setClosable(false);

        tabPane.getTabs().add(java);
        tabPane.getTabs().add(cpp);
        tabPane.setMaxSize(800, 400);
        tabPane.setMinSize(800, 400);

        //ПРОБНАЯ ТАБЛИЦА!
        TabPane j = new TabPane();
        int numberOfGroupJ = 2;// ПОЛУЧИТЬ
        String GroupsJ = "ИИБ-3-14";
        //for (int i=0; i< numberOfGroupJ; numberOfGroupJ++)
        //{
        Tab tab = new Tab();
        tab.setText(GroupsJ);
        tab.setClosable(false);
        j.getTabs().add(tab);
        java.setContent(j);
        Tab t = j.getTabs().get(0);
        TableView newTable = firstTable;


        int numberOfLabs = 2;
        for (int i = 0; i < numberOfLabs; i++) {
            TableColumn laba = new TableColumn();
            int b=i+1;
            laba.setText("Лаб.№ "+ b);
            newTable.getColumns().add(laba);
        }
        t.setContent(newTable);
        //}
        //КОНЕЦ ТАБЛИЦЫ

        main.getChildren().add(new Label("Оценка программы" + "\n" + "\n"));
        main.getChildren().add(tabPane);
        main.setAlignment(Pos.TOP_CENTER);
        visibleField.getChildren().add(main);
    }
}