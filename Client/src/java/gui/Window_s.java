package gui;

import connect.ConnectToServer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import query.LoginRequest;
import query.RegisterRequest;
import query.StatsRequest;
import query.TestUploadRequest;
import reply.Stats;
import reply.TestUploading;
import reply.User;
import tester.Tester;
import transfer.LabSubmitDate;
import transfer.LabsPossible;
import transfer.StudentResult;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jay on 02.07.2015.
 * Класс с изображениями окон
 */
public class Window_s {

    protected Scene main;
    protected VBox visibleField;
    boolean check=false;
    int page=3; //номер окна. 1-вход, 2-регистрация,3-для студентов,4-для перподавателей
    User newUser = new User();
    ForTest forTest = new ForTest();
    StatsRequest statsRequest = new StatsRequest();
    ConnectToServer connect = new ConnectToServer();

    public Window_s() {
        VBoxBuilder vboxBld = VBoxBuilder.create();
        visibleField = vboxBld.build();
        SceneBuilder scnBld = SceneBuilder.create();
        scnBld.fill(Paint.valueOf("grey"));
        scnBld.stylesheets(Main.class.getResource("window.css").toExternalForm());
        scnBld.height(570);
        scnBld.width(900);
        Window();
        scnBld.root(visibleField);
        visibleField.layout();
        main = scnBld.build();
    }

    public void Window () //смена окон
    {
        System.out.println(page + " page");
        if (page==1)
            first();
        else if (page==2)
            registration();
        else if (page==3)
        {
           if (!newUser.isLecturer)
               forStudents();
           else
               forTeachers();
        }
        else if (page==4)
            downloadTests();
    }

    public void first() //Окно входа
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
        //Комментарии
        HBox comments = new HBox();
        final Label label = new Label();

        HBox errorL = new HBox();
        HBox errorP = new HBox();
        errorL.setAlignment(Pos.CENTER);
        errorP.setAlignment(Pos.CENTER);
        final Label errorLogin = new Label();
        final Label errorPass = new Label();
        errorLogin.setAlignment(Pos.CENTER);
        errorPass.setAlignment(Pos.CENTER);

        // Кнопки
        HBox forButtons = new HBox();
        Button enter = new Button("Вход");
        enter.setOnAction(new EventHandler<ActionEvent>(){
                              @Override
                              public void handle(javafx.event.ActionEvent actionEvent) {
                                  errorLogin.setText("");
                                  errorPass.setText("");
                                  if ((textForLogin.getText().isEmpty())||(textForPassword.getText().isEmpty())||(!textForLogin.getText().matches("\\w{3,}"))||(!textForPassword.getText().matches("\\w{6,}"))) {
                                      if (textForLogin.getText().isEmpty()) {
                                          check = false;
                                          errorLogin.setText("Введите логин.");
                                          errorLogin.setStyle("-fx-font-style:italic;");
                                      }
                                      else if (!textForLogin.getText().matches("\\w{3,}")) {
                                          check = false;
                                          errorLogin.setText("Неверный ввод. Введите более 3 английских букв/цифр.");
                                          errorLogin.setStyle("-fx-font-style:italic;");
                                      }
                                      else errorLogin.setText("");
                                      if (textForPassword.getText().isEmpty()) {
                                          check = false;
                                          errorPass.setText("Введите пароль.");
                                          errorPass.setStyle("-fx-font-style:italic;");
                                      }
                                      else if (!textForPassword.getText().matches("\\w{6,}")) {
                                          errorPass.setText("Неверный ввод. Введите более 6 английских букв/цифр.");
                                          errorPass.setStyle("-fx-font-style:italic;");
                                          check = false;
                                      }
                                      else errorPass.setText("");
                                  }
                                      else {
                                          LoginRequest loginApply = new LoginRequest();
                                          loginApply.login = textForLogin.getText();
                                          loginApply.password = textForPassword.getText();
                                          check = connect.LoginIn(loginApply);
                                          if (check) {
                                              newUser = connect.UserRequest();
                                              label.setText("");
                                              page = 3;
                                              Window();
                                          } else {
                                              label.setText(connect.ErrorRequest());
                                              label.setStyle("-fx-font-style:italic;");
                                          }
                                      }
                              }
                          }
        );

        Button registration = new Button("Регистрация");
        registration.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 2;
                Window();
            }
        });

        //СПРАВКА
        Button help = new Button("Справка");
        help.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Help.openHelp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox fH = new VBox();
        HBox forHelp = new HBox();
        forHelp.setMaxSize(850,20);
        forHelp.setMinSize(850,20);
        help.setAlignment(Pos.TOP_RIGHT);
        forHelp.getChildren().add(help);
        forHelp.setTranslateX(770);
        forHelp.setTranslateY(-140);
       visibleField.getChildren().add(forHelp);

        HBox vxod = new HBox();
        Label text = new Label("  Вход в систему ");

        text.setStyle("-fx-font-size: 44 px; -fx-font-style: italic;");
        vxod.getChildren().addAll(text);
        vxod.setAlignment(Pos.CENTER);


        comments.getChildren().addAll(label);
        forButtons.getChildren().addAll(enter, registration);
        forButtons.setSpacing(10);
        forButtons.setTranslateX(390);


        errorL.getChildren().add(errorLogin);
        errorP.getChildren().add(errorPass);
        errorL.setTranslateX(1);
        errorP.setTranslateX(5);
        comments.setAlignment(Pos.CENTER);
        fH.getChildren().addAll(vxod, new Label("\n"), new Label("\n"), forLogin, errorL, forPassword, errorP, new Label("\n"), comments, new Label("\n"), forButtons);
        visibleField.getChildren().addAll(fH);
        visibleField.setAlignment(Pos.CENTER);
        }

    public void registration () //Окно регистрации
    {
        visibleField.getChildren().clear();
        final int[] done = {-1};
        //Имя
        HBoxBuilder hBoxBuilder = HBoxBuilder.create();
        HBox forName = hBoxBuilder.build();
        final Label name = new Label("Имя:");
        Tooltip tipName = new Tooltip();
        tipName.setText("Только русские буквы");

        final TextField textForName = new TextField();
        textForName.setPromptText("Иван");
        textForName.setEditable(true);
        textForName.setTooltip(tipName);
        forName.getChildren().addAll(name, textForName);
        forName.setSpacing(44);
        forName.setAlignment(Pos.CENTER);
        //Фамилия
        HBox forSurname = hBoxBuilder.build();
        Label surname = new Label("Фамилия:");
        Tooltip tipSur = new Tooltip();
        tipSur.setText("Только русские буквы");
        final TextField textForSurname = new TextField();
        textForSurname.setTooltip(tipSur);
        textForSurname.setPromptText("Иванов");
        textForSurname.setEditable(true);
        forSurname.getChildren().addAll(surname, textForSurname);
        forSurname.setSpacing(14);
        forSurname.setAlignment(Pos.CENTER);
        // Отчество
        HBox forPatronymic = hBoxBuilder.build();
        final Label patronymic = new Label("Отчество:");
        Tooltip tipSec = new Tooltip();
        tipSec.setText("Только русские буквы");
        final TextField textForPatronymic = new TextField();
        textForPatronymic.setPromptText("Иванович");
        textForPatronymic.setTooltip(tipSec);
        textForPatronymic.setEditable(true);
        forPatronymic.getChildren().addAll(patronymic, textForPatronymic);
        forPatronymic.setSpacing(15);
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
        Tooltip tipLogin = new Tooltip();
        tipLogin.setText("Английские буквы или цифры");
        final TextField textForLogin = new TextField();
        textForLogin.setTooltip(tipLogin);
        textForLogin.setPromptText("не менее 3 символов");
        textForLogin.setEditable(true);
        forLogin.getChildren().addAll(login, textForLogin);
        forLogin.setSpacing(32);
        forLogin.setAlignment(Pos.CENTER);
        // Пароль
        HBox forPassword = hBoxBuilder.build();
        final Label password = new Label("Пароль:");
        Tooltip tipPass = new Tooltip();
        tipPass.setText("Английские буквы или цифры");
        final PasswordField textForPassword = new PasswordField();
        textForPassword.setTooltip(tipPass);
        textForPassword.setPromptText("не менее 6 символов");
        textForPassword.setEditable(true);
        forPassword.getChildren().addAll(password, textForPassword);
        forPassword.setSpacing(24);
        forPassword.setAlignment(Pos.CENTER);

        final Label label = new Label();
        HBox errorSur = new HBox();
        final HBox errorN = new HBox();
        final HBox errorSec = new HBox();
        HBox errorGr = new HBox();
        HBox errorL = new HBox();
        HBox errorP = new HBox();
        final Label errorSurname = new Label();
        final Label errorName = new Label();
        final Label errorSecond = new Label();
        final Label errorGroup = new Label();
        final Label errorLogin = new Label();
        final Label errorPass = new Label();

        // Кнопки
        final HBox forS = new HBox();
        final Button success = new Button("Регистрация прошла успешно");
        success.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page=1;
                Window();
            }
        });
        HBox forButtons = hBoxBuilder.build();
        Button registration = new Button("Зарегистрироваться");
        registration.setOnAction(new EventHandler<ActionEvent>() {
                                     @Override
                                     public void handle(javafx.event.ActionEvent actionEvent) {

                                         errorName.setText("");
                                         errorSurname.setText("");
                                         errorSecond.setText("");
                                         errorGroup.setText("");
                                         errorLogin.setText("");
                                         errorPass.setText("");
                                         if (done[0]==-1) {
                                             if ((textForLogin.getText().isEmpty()) || (textForName.getText().isEmpty()) || (textForSurname.getText().isEmpty()) ||
                                                     (textForGroup.getText().isEmpty()) || (textForPassword.getText().isEmpty()) || (textForPatronymic.getText().isEmpty()) ||
                                                     (!textForName.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) || (!textForSurname.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) ||
                                                     (!textForPatronymic.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) || (!textForGroup.getText().matches("([А-Я]|Ё){3}\\-\\d+\\-[1-9]{2}"))
                                                     || (!textForLogin.getText().matches("\\w{3,}")) || (!textForPassword.getText().matches("\\w{6,}"))) {
                                                 check = false;
                                                 if (textForName.getText().isEmpty()) {
                                                     errorName.setText("  Введите имя.");
                                                     errorName.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else if (!textForName.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) {
                                                     errorName.setText("Неверный ввод. Введите только русские буквы, первая буква - заглавная.");
                                                     errorName.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else errorName.setText("");

                                                 if (textForSurname.getText().isEmpty()) {
                                                     errorSurname.setText("             Введите фамилию.");
                                                     errorSurname.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else if (!textForSurname.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) {
                                                     errorSurname.setText("Неверный ввод. Введите только русские буквы, первая буква - заглавная.");
                                                     errorSurname.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else errorSurname.setText("");

                                                 if (textForPatronymic.getText().isEmpty()) {
                                                     errorSecond.setText("              Введите отчество.");
                                                     errorSecond.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else if (!textForPatronymic.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) {
                                                     errorSecond.setText("Неверный ввод. Введите только русские буквы, первая буква - заглавная.");
                                                     errorSecond.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else errorSecond.setText("");

                                                 if (textForGroup.getText().isEmpty()) {
                                                     errorGroup.setText("        Введите группу.");
                                                     errorGroup.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else if (!textForGroup.getText().matches("([А-Я]|Ё){3}\\-\\d+\\-[1-9]{2}")) {
                                                     errorGroup.setText("Неверный ввод.");
                                                     errorGroup.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else errorGroup.setText("");

                                                 if (textForPassword.getText().isEmpty()) {
                                                     errorPass.setText("       Ведите пароль.");
                                                     errorPass.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else if (!textForPassword.getText().matches("\\w{6,}")) {
                                                     errorPass.setText("Неверный ввод. Введите более 6 английских букв/цифр.");
                                                     errorPass.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else errorPass.setText("");

                                                 if (textForLogin.getText().isEmpty()) {
                                                     errorLogin.setText("      Введите логин.");
                                                     errorLogin.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else if (!textForLogin.getText().matches("\\w{3,}")) {
                                                     errorLogin.setText("Неверный ввод. Введите более 3 английских букв/цифр.");
                                                     errorLogin.setStyle("-fx-font-style:italic;");
                                                 }
                                                 else errorLogin.setText("");
                                             } else {
                                                 RegisterRequest registerApply = new RegisterRequest();
                                                 registerApply.isLecturer = false;
                                                 registerApply.name = textForName.getText();
                                                 registerApply.surname = textForSurname.getText();
                                                 registerApply.secondName = textForPatronymic.getText();
                                                 registerApply.group = textForGroup.getText();
                                                 registerApply.login = textForLogin.getText();
                                                 registerApply.password = textForPassword.getText();
                                                 check = connect.RegisterUser(registerApply);
                                                 if (check) {
                                                     done[0] = 1;
                                                     errorName.setText("");
                                                     errorSecond.setText("");
                                                     errorSurname.setText("");
                                                     errorGroup.setText("");
                                                     errorLogin.setText("");
                                                     errorPass.setText("");
                                                     forS.getChildren().add(success);
                                                     forS.setAlignment(Pos.CENTER);
                                                 } else {
                                                     label.setText(connect.ErrorRequest());
                                                     label.setStyle("-fx-font-style:italic;");
                                                 }
                                             }
                                         }
                                     }
                                 }
        );
        errorN.getChildren().add(errorName);
        errorGr.getChildren().add(errorGroup);
        errorL.getChildren().add(errorLogin);
        errorP.getChildren().add(errorPass);
        errorSur.getChildren().add(errorSurname);
        errorSec.getChildren().add(errorSecond);

        errorN.setAlignment(Pos.CENTER);
        errorGr.setAlignment(Pos.CENTER);
        errorL.setAlignment(Pos.CENTER);
        errorP.setAlignment(Pos.CENTER);
        errorSur.setAlignment(Pos.CENTER);
        errorSec.setAlignment(Pos.CENTER);

        visibleField.getChildren().addAll(forSurname, errorSur, forName, errorN, forPatronymic, errorSec, forGroup, errorGr, forLogin, errorL, forPassword, errorP, new Label("\n"));
        visibleField.getChildren().add(forS);
        Button cancel = new Button("Отмена");
        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 1;
                Window();
            }
        });
        forButtons.getChildren().addAll(registration, cancel);
        forButtons.setAlignment(Pos.CENTER);
        forButtons.setSpacing(10);

        visibleField.getChildren().addAll(label, new Label("\n"));
        visibleField.getChildren().add(forButtons);
        visibleField.setAlignment(Pos.CENTER);
    }

    public void forStudents ()
    {
        visibleField.getChildren().clear();
        VBoxBuilder vboxBld = VBoxBuilder.create();
        VBox leftVBox = vboxBld.build();
        VBox rightVBox = vboxBld.build();

        rightVBox.setMinWidth(230);
        leftVBox.getChildren().addAll(new Label(newUser.surname + " " + newUser.name + " " + newUser.secondName, new Label("\n")));
        leftVBox.getChildren().addAll(new Label(" " + newUser.group));
        rightVBox.getChildren().addAll(new Label("\n"), new Label("\n"), new Label("\n"), new Label("\n"));

        final GridPane pane = new GridPane();

        final HBox Highline = new HBox(); // текст
        Label test = new Label("Введите код в поле или загрузите соответствующий файл");
        test.setAlignment(Pos.CENTER_RIGHT);
        Highline.getChildren().add(test);
        Highline.setAlignment(Pos.CENTER);
        leftVBox.getChildren().addAll(Highline, new Label("\n"));

        final TextArea textArea = new TextArea();
        textArea.setMinSize(500,280);
        textArea.setMaxSize(500,280);
        textArea.setEditable(true);
        leftVBox.getChildren().add(textArea);

        final Label testResult = new Label("");
        testResult.setMinSize(230, 15);
        testResult.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: black; -fx-border: 3px;");
        testResult.setAlignment(Pos.CENTER);
        rightVBox.getChildren().addAll(testResult, new Label("\n"));
        HBox sub = new HBox(8); // предмет
        VBox one = new VBox();
        one.setMaxWidth(66);
        one.setMinWidth(66);
        one.setAlignment(Pos.CENTER);
        VBox two = new VBox();
        HBox sem = new HBox(81); // семестр
        VBox one_1 = new VBox();
        one_1.setMaxWidth(82);
        one_1.setMinWidth(82);
        one_1.setAlignment(Pos.CENTER);
        VBox two_1 = new VBox();
        HBox task = new HBox(39); // лаба
        VBox one_2 = new VBox();
        one_2.setMaxWidth(125);
        one_2.setMinWidth(125);
        one_2.setAlignment(Pos.CENTER);
        VBox two_2 = new VBox();
        HBox variant = new HBox(81); // вариант
        VBox one_3 = new VBox();
        one_3.setMaxWidth(82);
        one_3.setMinWidth(82);
        one_3.setAlignment(Pos.CENTER);
        VBox two_3 = new VBox();

        Label subject = new Label("Предмет ");
        Label term = new Label("Семестр № ");
        Label lab = new Label("Лабораторная № ");
        Label var = new Label("Вариант № ");
        final ComboBox forSubject = new ComboBox();
        final ComboBox forTerm = new ComboBox();
        final ComboBox forLab = new ComboBox();
        final ComboBox forVariant = new ComboBox();

        forSubject.setMaxSize(150, 25);
        forTerm.setMaxSize(60, 25);
        forLab.setMaxSize(60, 25);
        forVariant.setMaxSize(60, 25);
        forSubject.setMinSize(150, 25);
        forTerm.setMinSize(60, 25);
        forLab.setMinSize(60, 25);
        forVariant.setMinSize(60, 25);

        one.getChildren().add(subject);
        two.getChildren().add(forSubject);
        one_1.getChildren().add(term);
        two_1.getChildren().add(forTerm);
        one_2.getChildren().add(lab);
        two_2.getChildren().add(forLab);
        one_3.getChildren().add(var);
        two_3.getChildren().add(forVariant);


        //Добавлено добрыми феями
        TreeSet<Integer> subjects = new TreeSet<>();
        if(newUser.labInfo != null) {
            for (LabsPossible lp : newUser.labInfo)
                subjects.add(lp.term);
            for(Integer i : subjects)
                forTerm.getItems().add(i.toString());
        }
        forTerm.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                //Добавлено добрыми феями
                forSubject.getItems().clear();
                forLab.getItems().clear();
                forVariant.getItems().clear();
                forTest.term=Integer.valueOf(t1);
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.term.toString().equals(t1)) {
                        forSubject.getItems().add(lp.subject);
                    }
            }
        });

            forSubject.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    //Добавлено добрыми феями
                    forLab.getItems().clear();
                    forVariant.getItems().clear();
                    forTest.subject=t1;
                    int number = 0;
                    for(LabsPossible lp : newUser.labInfo)
                        if(lp.subject.equals(t1) && lp.term.toString().equals(forTerm.getValue())) {
                            number = lp.variants.size();
                            break;
                        }
                    for(int i = 1; i <= number; i++) forLab.getItems().add(Integer.toString(i));
                }
            });


        forLab.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                //Добавлено добрыми феями
                forVariant.getItems().clear();
                forTest.number=Integer.valueOf(t1);
                int variant = 0;
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.subject.equals(forSubject.getValue()) && lp.term.toString().equals(forTerm.getValue())) {
                        variant = lp.variants.get(Integer.parseInt(t1)-1);
                        break;
                    }
                for(int i = 1; i <= variant; i++) forVariant.getItems().add(Integer.toString(i));
            }
        });

        forVariant.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                forTest.variant=Integer.valueOf(t1);
            }
        });

        sub.getChildren().addAll(one, two);
        sem.getChildren().addAll(one_1,two_1);
        task.getChildren().addAll(one_2, two_2);
        variant.getChildren().addAll(one_3, two_3);
        rightVBox.getChildren().addAll(sem, new Label("\n"), sub, new Label("\n"), task, new Label("\n"), variant);
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
                chooseCode.setStyle("-fx-background-color: linear-gradient(rgba(0, 98, 184, 0.5), rgba(0, 98, 184, 0.9));" +
                        "radial-gradient(center 50% -30%, radius 200%, #0080f0 30%, #00498a 50%);");
                result[0]=1;
                label.setText("");
                if (chooseFile.getText().equals("Проверяется файл"))
                {
                    chooseFile.setText("Проверять файл");
                    chooseFile.setStyle("-fx-background-color: linear-gradient(rgba(36, 153, 255, 0.5), rgba(36, 153, 255, 0.9));" +
                            "radial-gradient(center 50% -30%, radius 200%, #57b0ff 30%, #0080f0 50%);");
                }
            }
        });

        chooseFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                chooseFile.setText("Проверяется файл");
                chooseFile.setStyle("-fx-background-color: linear-gradient(rgba(0, 98, 184, 0.5), rgba(0, 98, 184, 0.9));" +
                        " radial-gradient(center 50% -30%, radius 200%, #0080f0 30%, #00498a 50%);");
                result[0]=2;
                label.setText("");
                if (chooseCode.getText().equals("Проверяется код"))
                {
                    chooseCode.setText("Проверять код");
                    chooseCode.setStyle("-fx-background-color: linear-gradient(rgba(36, 153, 255, 0.5), rgba(36, 153, 255, 0.9));" +
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
                if (forTest.subject!=null) { //что-то выбрано
                    if (forTest.subject.equals("Программирование"))
                    {
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("jar file", "jar");
                        laba.removeChoosableFileFilter(laba.getAcceptAllFileFilter());
                        laba.setFileFilter(filter);
                        laba.setDialogTitle("Choose a file");
                        int returnVal = laba.showOpenDialog(null);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            file[0]=laba.getSelectedFile();
                            done.setText("Файл загружен");
                        }
                    }
                        else if (forTest.subject.equals("АиСД"))
                    {
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("exe file", "exe");
                        laba.removeChoosableFileFilter(laba.getAcceptAllFileFilter());
                        laba.setFileFilter(filter);
                        laba.setDialogTitle("Choose a file");
                        int returnVal = laba.showOpenDialog(null);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            file[0]=laba.getSelectedFile();
                            done.setText("Файл загружен");
                        }
                    }
                    else done.setText("");
                } else done.setText("Выберите предмет");
                files.getChildren().add(done);
            }
        });
        files.getChildren().add(openL);
        leftVBox.getChildren().addAll(choose, new Label("\n"),files, new Label("\n"), new Label("Для создания exe файлов требуется предустановленный g++ компилятор"), new Label("\n"), new Label("Для создания jar файлов требуется предустановленный Ant"));

        Button exit = new Button("Выйти");
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 1;
                Window();
            }
        });

        //ЗАГРУЗИТЬ ТЕСТ
        Button toLoadTest = new Button("Запустить тест");
        final Label mistakesInCode = new Label();
        final Label mistakesInCom = new Label();
        final Label mistakesInTerm = new Label();
        final Label mistakesInLab = new Label();
        final Label mistakesInVar = new Label();
        rightVBox.getChildren().addAll(new Label("\n"), toLoadTest, new Label("\n"));
        final VBox newVbox = new VBox();
        toLoadTest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newVbox.getChildren().clear();
                if (result[0] == 0)
                    mistakesInCode.setText("Выберите вариант проверки");
                else {
                    if (forTest.subject == null) //если не выбран предмет
                    {
                        mistakesInCom.setText("Выберите предмет");
                    }
                    else mistakesInCom.setText("");
                    if (forTest.term == -1) // не выбран семестр
                        mistakesInTerm.setText("Выберите семестр");
                    else mistakesInTerm.setText("");
                    if (forTest.number == -1) // не выбран номер лабы
                        mistakesInLab.setText("Выберите номер лабораторной");
                    else mistakesInLab.setText("");
                    if (forTest.variant == -1) // не выбран вариант
                        mistakesInVar.setText("Выберите вариант");
                    else mistakesInVar.setText("");
                    if ((file[0] != null) || (!textArea.getText().isEmpty())) // если есть лаба
                    {
                        if (result[0] == 1) {
                            forTest.code = textArea.getText();
                            forTest.laba = null;
                        }
                        if (result[0] == 2) {
                            forTest.laba = file[0];
                            forTest.code = null;
                        }
                        mistakesInCode.setText("");
                    } else
                        mistakesInCode.setText("Введите код/добавьте файл");
                }
                if (!mistakesInCode.getText().equals(""))
                    newVbox.getChildren().add(mistakesInCode);
                if (!mistakesInTerm.getText().equals(""))
                    newVbox.getChildren().add(mistakesInTerm);
                if (!mistakesInCom.getText().equals(""))
                    newVbox.getChildren().add(mistakesInCom);
                if (!mistakesInLab.getText().equals(""))
                    newVbox.getChildren().add(mistakesInLab);
                if (!mistakesInVar.getText().equals(""))
                    newVbox.getChildren().add(mistakesInVar);
                if (mistakesInCode.getText().equals("")&&(mistakesInTerm.getText().equals(""))&&mistakesInCom.getText().equals("")&&mistakesInLab.getText().equals("")&&mistakesInVar.getText().equals(""))
                testResult.setText(Tester.labTestExecute(forTest));
            }
        });


        newVbox.setMaxHeight(150);
        newVbox.setMinHeight(150);

        final HBox forResults = new HBox();
        final Label r = new Label("");


        final ComboBox chSub = new ComboBox();
        final ComboBox chTerm = new ComboBox();
        chSub.setMaxWidth(150);
        chSub.setMinWidth(150);
        chTerm.setMaxWidth(50);
        chTerm.setMinWidth(50);

        final StatsRequest statsRequest = new StatsRequest();

        TreeSet<Integer> subj = new TreeSet<>();
        if(newUser.labInfo != null) {
            for (LabsPossible lp : newUser.labInfo)
                subj.add(lp.term);
            for(Integer i : subj)
                chTerm.getItems().add(i.toString());
        }
        chTerm.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                chSub.getItems().clear();
                statsRequest.term=Integer.valueOf(t1);
                statsRequest.subject=null;
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.term.toString().equals(t1)) {
                        chSub.getItems().add(lp.subject);
                    }
            }
        });

        forSubject.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                statsRequest.subject = t1;
                newVbox.getChildren().clear();
                forResults.getChildren().clear();
                r.setText("Результаты:");
                forResults.getChildren().add(r);
                forResults.setAlignment(Pos.CENTER);
                statsRequest.id=newUser.id;
                String studentResult = connect.userStatsRequest(statsRequest);
                newVbox.getChildren().addAll(forResults, new Label("\n") , new Label(studentResult));
            }
        });

        //Результаты по лабам
        Button results = new Button("Узнать результаты");
        results.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newVbox.getChildren().clear();
                newVbox.getChildren().addAll(new Label("Выберите семестр:"), chTerm, new Label("\n"), new Label("Выберите предмет:"), chSub);
            }
        });






        leftVBox.getChildren().addAll(new Label("\n"), results);
        rightVBox.getChildren().addAll(newVbox,  new Label("\n"), new Label("\n"), new Label("\n"));

        //ВЫЙТИ
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        exit.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(exit);
        rightVBox.getChildren().add(hBox);

        pane.setHgap(30);
        pane.addColumn(0, leftVBox);
        pane.addColumn(1, rightVBox);
        pane.setMinSize(850,550);
        pane.setAlignment(Pos.CENTER);
        visibleField.getChildren().add(pane);
    }

    public void forTeachers() {

        visibleField.getChildren().clear();

        VBox main = new VBox();
        //ТАБЛИЧКА
        final TableView firstTable = new TableView();
        firstTable.setMaxSize(800, 400);
        firstTable.setMinSize(800, 400);
        firstTable.setEditable(false);
        firstTable.setVisible(false);
        firstTable.setItems(getTableData());

        TableColumn fio = new TableColumn();
        firstTable.getColumns().setAll(fio);
        fio.setText("ФИО");

        fio.setCellValueFactory(new PropertyValueFactory<Student,String>("fio"));
        if(firstTable.getItems().size() != 0)
        for(int i=0;i<((Student)firstTable.getItems().get(0)).dates.size();i++)
        {
            TableColumn<Student,String> lab = new TableColumn("Лаб.№ " + (i+1));
            lab.setCellValueFactory(new PropertyValueFactory("lab" + (i+1) + "Date"));
            firstTable.getColumns().add(lab);
        }

//КОНЕЦ ТАБЛИЦЫ


        HBox choose = new HBox();

        HBox sem = new HBox(10);
        HBox sub = new HBox(10);
        HBox group = new HBox(10);

        final ComboBox forSem = new ComboBox();
        final ComboBox forSub = new ComboBox();
        final ComboBox forGroup = new ComboBox();
        forSub.setMaxSize(150, 25);
        forSub.setMinSize(150, 25);
        forSem.setMaxSize(60, 25);
        forSem.setMinSize(60, 25);
        forGroup.setMaxSize(100, 25);
        forGroup.setMinSize(100, 25);

        sem.getChildren().addAll(new Label("Семестр"), forSem);
        sub.getChildren().addAll(new Label("Предмет"), forSub);
        group.getChildren().addAll(new Label("Группа"), forGroup);
        choose.setSpacing(20);

        //Дела добрых фей
        TreeSet<Integer> subjects = new TreeSet<>();
        if(newUser.labInfo != null) {
            for (LabsPossible lp : newUser.labInfo)
                subjects.add(lp.term);
            for(Integer i : subjects)
                forSem.getItems().add(i.toString());
        }

        forSem.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                forSub.getItems().clear();
                forGroup.getItems().clear();
                statsRequest.term = Integer.valueOf(t1);
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.term.toString().equals(t1)) {
                        forSub.getItems().add(lp.subject);
                    }
            }
        });

        forSub.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                forGroup.getItems().clear();
                statsRequest.subject = t1;
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.subject.equals(t1) && lp.term.toString().equals(forSem.getValue())) {
                        forGroup.getItems().addAll(lp.groups);
                        break;
                    }
            }
        });

        forGroup.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                statsRequest.group = t1;
                firstTable.setVisible(true);
            }
        });

        choose.getChildren().addAll(sem,sub,group);

        HBox hBox = new HBox(15);
        Button exit = new Button("Выход");
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 1;
                Window();
            }
        });
        Button download = new Button("Загрузить тест");
        download.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page=4;
                Window();
            }
        });
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        exit.setAlignment(Pos.BOTTOM_RIGHT);
        download.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(download, exit, new Label("            "));


        main.getChildren().add(new Label("\n"+"Таблица результатов" + "\n" + "\n"));
        choose.setAlignment(Pos.CENTER);
        main.getChildren().addAll(choose, new Label("\n"), firstTable, new Label("\n"), hBox);
        main.setAlignment(Pos.TOP_CENTER);
        visibleField.getChildren().add(main);
    }

    private ObservableList getTableData()
    {
        List list_1 = new ArrayList();
        Stats studentsResults = connect.lecturerStatsRequest(statsRequest);
        if ((studentsResults != null) && (studentsResults.list != null)) {
            for (int i = 0; i < studentsResults.list.size(); i++)
                try {
                    list_1.add(new Student(studentsResults.list.get(i)));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
        return FXCollections.observableList(list_1);
    }

    public class Student {

        private SimpleStringProperty fio;
        public SimpleStringProperty lab1Date;
        public SimpleStringProperty lab2Date;
        public SimpleStringProperty lab3Date;
        public SimpleStringProperty lab4Date;
        public SimpleStringProperty lab5Date;
        public SimpleStringProperty lab6Date;
//Пока только заданное количество лаб.

        public ArrayList<LabSubmitDate> dates;

        Student() {
            fio = new SimpleStringProperty();
            dates = new ArrayList<>();
            lab1Date = new SimpleStringProperty("NULL Date");
            lab2Date = new SimpleStringProperty("NULL Date");
            lab3Date = new SimpleStringProperty("NULL Date");
            lab4Date = new SimpleStringProperty("NULL Date");
            lab5Date = new SimpleStringProperty("NULL Date");
            lab6Date = new SimpleStringProperty("NULL Date");
        }

        Student(StudentResult result) throws NoSuchFieldException, IllegalAccessException {
            this.fio = new SimpleStringProperty(result.surname + result.name + result.secondName);
            this.dates = result.dates;
            setAllDates();
        }

        public String getFio() {
            return fio.get();
        }

        public SimpleStringProperty fioProperty() {
            return fio;
        }

        public void setFio(String fio)
        {
            this.fio.set(fio);
        }

        public void setAllDates() throws NoSuchFieldException, IllegalAccessException {
            for(int i=0;i<this.dates.size();i++)
            {
                if(this.dates.get(i) != null)
                    this.getClass().getField("lab" + (i + 1) + "Date").set(this, new SimpleStringProperty(this.dates.get(i).year + "-" +
                            this.dates.get(i).month + "-" +
                            this.dates.get(i).day));
                else
                    this.getClass().getField("lab" + (i + 1) + "Date").set(this, new SimpleStringProperty("NullDate"));
            }

        }

        public SimpleStringProperty getLab1Date() {
            return lab1Date;
        }

        public SimpleStringProperty lab1DateProperty(){
            return lab1Date;
        }

        public void setLab1Date(SimpleStringProperty lab1Date) {
            this.lab1Date = lab1Date;
        }

        public SimpleStringProperty getLab2Date() {
            return lab2Date;
        }

        public SimpleStringProperty lab2DateProperty(){
            return lab2Date;
        }

        public void setLab2Date(SimpleStringProperty lab2Date) {
            this.lab2Date = lab2Date;
        }

        public SimpleStringProperty getLab3Date() {
            return lab3Date;
        }

        public SimpleStringProperty lab3DateProperty(){
            return lab3Date;
        }

        public void setLab3Date(SimpleStringProperty lab3Date) {
            this.lab3Date = lab3Date;
        }

        public SimpleStringProperty getLab4Date() {
            return lab4Date;
        }

        public SimpleStringProperty lab4DateProperty(){
            return lab4Date;
        }

        public void setLab4Date(SimpleStringProperty lab4Date) {
            this.lab4Date = lab4Date;
        }

        public SimpleStringProperty getLab5Date() {
            return lab5Date;
        }

        public SimpleStringProperty lab5DateProperty(){
            return lab5Date;
        }

        public void setLab5Date(SimpleStringProperty lab5Date) {
            this.lab5Date = lab5Date;
        }

        public SimpleStringProperty getLab6Date() {
            return lab6Date;
        }

        public SimpleStringProperty lab6DateProperty(){
            return lab6Date;
        }

        public void setLab6Date(SimpleStringProperty lab6Date) {
            this.lab6Date = lab6Date;
        }
    }

    public void downloadTests()
    {
        visibleField.getChildren().clear();
        VBox vBox = new VBox();

        vBox.setAlignment(Pos.CENTER);
        HBox term = new HBox(58);
        term.setTranslateX(-16);
        HBox subject = new HBox(55);
        subject.setTranslateX(33);
        HBox number = new HBox(12);
        number.setTranslateX(-16);
        HBox variant = new HBox(40);
        variant.setTranslateX(-16);

        final ComboBox isTerm = new ComboBox();
        isTerm.setMinWidth(50);
        isTerm.setMaxWidth(50);
        final ComboBox isSubject = new ComboBox();
        isSubject.setMinWidth(150);
        isSubject.setMaxWidth(150);
        final ComboBox isNumber = new ComboBox();
        isNumber.setMinWidth(50);
        isNumber.setMaxWidth(50);
        final ComboBox isVariant = new ComboBox();
        isVariant.setMinWidth(50);
        isVariant.setMaxWidth(50);

        final TestUploadRequest test = new TestUploadRequest();

        TreeSet<Integer> subjects = new TreeSet<>();
        if(newUser.labInfo != null) {
            for (LabsPossible lp : newUser.labInfo)
                subjects.add(lp.term);
            for(Integer i : subjects)
                isTerm.getItems().add(i.toString());
        }
        isTerm.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                isSubject.getItems().clear();
                isNumber.getItems().clear();
                isVariant.getItems().clear();
                test.term=Integer.valueOf(t1);
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.term.toString().equals(t1)) {
                        isSubject.getItems().add(lp.subject);
                    }
            }
        });

        isSubject.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                isNumber.getItems().clear();
                isVariant.getItems().clear();
                test.subject=t1;
                int number = 0;
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.subject.equals(t1) && lp.term.toString().equals(isTerm.getValue())) {
                        number = lp.variants.size();
                        break;
                    }
                for(int i = 1; i <= number; i++) isNumber.getItems().add(Integer.toString(i));
            }
        });

        isNumber.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                isVariant.getItems().clear();
                test.labNumber=Integer.valueOf(t1);
                int variant = 0;
                for(LabsPossible lp : newUser.labInfo)
                    if(lp.subject.equals(isSubject.getValue()) && lp.term.toString().equals(isTerm.getValue())) {
                        variant = lp.variants.get(Integer.parseInt(t1)-1);
                        break;
                    }
                for(int i = 1; i <= variant; i++) isVariant.getItems().add(Integer.toString(i));
            }
        });

        isVariant.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                test.variant=Integer.valueOf(t1);
            }
        });


        //ВХОД
        Button file = new Button("Загрузить входные данные");
        file.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFileChooser testIn = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file", "txt");
                testIn.removeChoosableFileFilter(testIn.getAcceptAllFileFilter());
                testIn.setFileFilter(filter);
                testIn.setDialogTitle("Choose a new test");
                int returnVal = testIn.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    test.input=testIn.getSelectedFile();
                }
            }
        });

        //ВЫХОД
        Button file_1 = new Button("Загрузить выходные данные");

        file_1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFileChooser testOut = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file", "txt");
                testOut.removeChoosableFileFilter(testOut.getAcceptAllFileFilter());
                testOut.setFileFilter(filter);
                testOut.setDialogTitle("Choose a new test");
                int returnVal = testOut.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    test.output=testOut.getSelectedFile();
                }
            }
        });

        final Label mistakesInCode = new Label();
        final Label mistakesInCom = new Label();
        final Label mistakesInTerm = new Label();
        final Label mistakesInLab = new Label();
        final Label mistakesInVar = new Label();
        final Label mistakesInInput = new Label();
        final Label mistakesInOutput = new Label();
        final VBox newVbox = new VBox();
        VBox send = new VBox();
        Button sendFile = new Button("Отправить файл");
        sendFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newVbox.getChildren().clear();
                if (test.subject == null) //если не выбран предмет
                {
                    mistakesInCom.setText("Выберите предмет");
                }
                else mistakesInCom.setText("");
                if (test.term == null) // не выбран семестр
                    mistakesInTerm.setText("Выберите семестр");
                else mistakesInTerm.setText("");
                if (test.labNumber == null) // не выбран номер лабы
                    mistakesInLab.setText("Выберите номер лабораторной");
                else mistakesInLab.setText("");
                if (test.variant == null) // не выбран вариант
                    mistakesInVar.setText("Выберите вариант");
                else mistakesInVar.setText("");
                if (test.input==null) //не выбраны данные на вход
                    mistakesInInput.setText("Выберите входные данные");
                else
                    mistakesInInput.setText("");
                if (test.output==null) //не выбраны данные на выход
                    mistakesInOutput.setText("Выберите выходные данные");
                else
                    mistakesInOutput.setText("");

                if (!mistakesInCode.getText().equals(""))
                    newVbox.getChildren().add(mistakesInCode);
                if (!mistakesInTerm.getText().equals(""))
                    newVbox.getChildren().add(mistakesInTerm);
                if (!mistakesInCom.getText().equals(""))
                    newVbox.getChildren().add(mistakesInCom);
                if (!mistakesInLab.getText().equals(""))
                    newVbox.getChildren().add(mistakesInLab);
                if (!mistakesInVar.getText().equals(""))
                    newVbox.getChildren().add(mistakesInVar);
                if (!mistakesInInput.getText().equals(""))
                    newVbox.getChildren().add(mistakesInInput);
                if (!mistakesInOutput.getText().equals(""))
                    newVbox.getChildren().add(mistakesInOutput);
                if (mistakesInCode.getText().equals("")&&(mistakesInTerm.getText().equals(""))&&mistakesInCom.getText().equals("")&&mistakesInLab.getText().equals("")&&mistakesInVar.getText().equals("")&&mistakesInInput.getText().equals("")&&mistakesInOutput.getText().equals(""))
                {
                    ConnectToServer c = new ConnectToServer();
                    c.TestUpload(test);
                }

            }
        });

        newVbox.setMaxHeight(150);
        newVbox.setMinHeight(150);
        newVbox.setAlignment(Pos.CENTER);
        send.setAlignment(Pos.CENTER);
        send.getChildren().addAll(sendFile, new Label("\n"), newVbox);


        HBox hBox = new HBox(45);
        Button back = new Button("Назад");
        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                page = 3;
                Window();
            }
        });
        hBox.getChildren().addAll(back, new Label("            "));

        term.getChildren().addAll(new Label("Семестр"), isTerm);
        term.setAlignment(Pos.CENTER);
        subject.getChildren().addAll(new Label("Предмет"), isSubject);
        subject.setAlignment(Pos.CENTER);
        number.getChildren().addAll(new Label("Лабраторная №"), isNumber);
        number.setAlignment(Pos.CENTER);
        variant.getChildren().addAll(new Label("Вариант №"), isVariant);
        variant.setAlignment(Pos.CENTER);

        file_1.setAlignment(Pos.CENTER);
        file.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(term, new Label("\n"), subject, new Label("\n"), number, new Label("\n"), variant, new Label("\n"), new Label("\n"), file, new Label("\n"), file_1, new Label("\n"), send);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.getChildren().addAll(new Label("\n"), hBox);
        vBox.setTranslateY(100);

        visibleField.getChildren().add(vBox);
    }
}