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
import reply.User;
import transfer.StudentResult;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 02.07.2015.
 * Класс с изображениями окон
 */
public class Window_s {

    protected Scene main;
    protected VBox visibleField;
    boolean check=false;
    int page=1; //номер окна. 1-вход, 2-регистрация,3-для студентов,4-для перподавателей
    User newUser = new User();
    ForTest forTest = new ForTest();
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
            //if (!newUser.isLecturer)
                forStudents();
                //else
                //forTeachers();
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

        // Кнопки
        HBox forButtons = new HBox();
        Button enter = new Button("Вход");
        enter.setOnAction(new EventHandler<ActionEvent>(){
                              @Override
                              public void handle(javafx.event.ActionEvent actionEvent) {
                                      if ((textForLogin.getText().isEmpty())||(textForPassword.getText().isEmpty())) {
                                          check = false;
                                          label.setText("Введите данные");
                                          label.setStyle("-fx-font-style:italic;");
                                      }
                                      else if((!textForLogin.getText().matches("\\w{3,}"))||(!textForPassword.getText().matches("\\w{6,}")))
                                      {
                                          check=false;
                                          label.setText("Неверно введены данные");
                                          label.setStyle("-fx-font-style:italic;");
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
        VBox fH = new VBox();
        HBox forHelp = new HBox();
        forHelp.setMaxSize(850,20);
        forHelp.setMinSize(850,20);
        help.setAlignment(Pos.TOP_RIGHT);
        forHelp.getChildren().add(help);
        forHelp.setTranslateX(770);
        forHelp.setTranslateY(-150);
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

            comments.setAlignment(Pos.CENTER);
        fH.getChildren().addAll(vxod, new Label("\n"), new Label("\n"), forLogin, forPassword, comments, forButtons);
            visibleField.getChildren().addAll(fH);
            visibleField.setAlignment(Pos.CENTER);
        }

    public void registration () //Окно регистрации
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
        forName.setSpacing(44);
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

        visibleField.getChildren().addAll(forSurname,forName, forPatronymic, forGroup, forLogin, forPassword);

        final Label label = new Label();

        // Кнопки
        HBox forButtons = hBoxBuilder.build();
        Button registration = new Button("Зарегистрироваться");
        registration.setOnAction(new EventHandler<ActionEvent>() {
                                     @Override
                                     public void handle(javafx.event.ActionEvent actionEvent) {

                                             if ((textForLogin.getText().isEmpty()) || (textForName.getText().isEmpty()) || (textForSurname.getText().isEmpty()) ||
                                                     (textForGroup.getText().isEmpty()) || (textForPassword.getText().isEmpty()) || (textForPatronymic.getText().isEmpty())) {
                                                 check = false;
                                                 label.setText("Неверно введены данные");
                                                 label.setStyle("-fx-font-style:italic;");

                                             } else if ((!textForName.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) || (!textForSurname.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) ||
                                                     (!textForPatronymic.getText().matches("([А-Я]|Ё)([а-я]|ё)+")) || (!textForGroup.getText().matches("([А-Я]|Ё){3}\\-\\d+\\-[1-9]{2}"))
                                                     || (!textForLogin.getText().matches("\\w{3,}")) || (!textForPassword.getText().matches("\\w{6,}"))) {
                                                 check = false;
                                                 label.setText("Неверно введены данные");
                                                 label.setStyle("-fx-font-style:italic;");
                                             } else {
                                                 System.out.println("все ок");
                                                 RegisterRequest registerApply = new RegisterRequest();
                                                 registerApply.isLecturer=false;
                                                 registerApply.name=textForName.getText();
                                                 registerApply.surname=textForSurname.getText();
                                                 registerApply.secondName=textForPatronymic.getText();
                                                 registerApply.group=textForGroup.getText();
                                                 registerApply.login=textForLogin.getText();
                                                 registerApply.password=textForPassword.getText();
                                                 check = connect.RegisterUser(registerApply);
                                                 if (check) {
                                                     label.setText("Регистрация прошла успешно");
                                                     label.setStyle("-fx-font-style:italic;");
                                                     page=1;
                                                     System.out.println("true");
                                                     Window();
                                                     /*try {
                                                         TimeUnit.SECONDS.sleep(3);
                                                     } catch (InterruptedException e) {
                                                         e.printStackTrace();
                                                     }*/
                                                 }
                                                 else
                                                 {
                                                     label.setText(connect.ErrorRequest());
                                                     label.setStyle("-fx-font-style:italic;");
                                                 }
                                             }
                                     }
                                 }
        );
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

        visibleField.getChildren().add(label);
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

        forTerm.getItems().addAll("1");
        forTerm.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                forTest.term = Integer.valueOf(t1);
                System.out.println(forTest.term);
                forSubject.getItems().addAll("Программирование", "АиСД");
            }
        });

            forSubject.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
                @Override
                public void changed(ObservableValue ov, String t, String t1) {
                    forTest.subject = t1;
                    System.out.println(forTest.subject);
                    forLab.getItems().addAll("1");
                }
            });


        forLab.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                forTest.number=Integer.valueOf(t1);
                System.out.println(forTest.number);
                forVariant.getItems().addAll("1");
            }
        });

        forVariant.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                forTest.variant=Integer.valueOf(t1);
                System.out.println(forTest.variant);
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
                    int rezult = laba.showDialog(null, "Открыть файл");
                    if (rezult == JFileChooser.APPROVE_OPTION) {
                        if (forTest.subject.equals("Программирование"))
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
                        else if (forTest.subject.equals("АиСД"))
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
                    //ПРОВЕРКА РЕЗУЛЬТАТА
                    final boolean res = false;
                    if (res)
                        testResult.setText("Тест прошел успешно");
                    else testResult.setText("Тест не пройден");
                }
                if (!mistakesInCode.equals(""))
                    newVbox.getChildren().add(mistakesInCode);
                if (!mistakesInTerm.equals(""))
                    newVbox.getChildren().add(mistakesInTerm);
                if (!mistakesInCom.equals(""))
                    newVbox.getChildren().add(mistakesInCom);
                if (!mistakesInLab.equals(""))
                    newVbox.getChildren().add(mistakesInLab);
                if (!mistakesInVar.equals(""))
                    newVbox.getChildren().add(mistakesInVar);
            }
        });


        newVbox.setMaxHeight(150);
        newVbox.setMinHeight(150);

        final HBox forResults = new HBox();
        final Label r = new Label("");
        //Результаты по лабам
        Button results = new Button("Узнать результаты");
        results.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newVbox.getChildren().clear();
                forResults.getChildren().clear();
                r.setText("Результаты:");
                forResults.getChildren().add(r);
                forResults.setAlignment(Pos.CENTER);
                newVbox.getChildren().addAll(forResults, new Label("\n"));
            }
        });

        leftVBox.getChildren().addAll(new Label("\n"), results);
        rightVBox.getChildren().addAll(newVbox,  new Label("\n"));

        //ВЫЙТИ
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        exit.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().addAll(exit);
        rightVBox.getChildren().add(hBox);

        //pane.setGridLinesVisible(true);
        pane.setHgap(30);
        pane.addColumn(0, leftVBox);
        pane.addColumn(1, rightVBox);
        pane.setMinSize(850,550);
        pane.setAlignment(Pos.CENTER);
        visibleField.getChildren().add(pane);
    }

    public void forTeachers() {

        visibleField.getChildren().clear();

        final int[] chooseSem = {-1};
        final String[] chooseSub = new String[1];
        final String[] chooseGroup = new String[1];

        VBox main = new VBox();
        //ТАБЛИЧКА
        final TableView firstTable = new TableView();
        firstTable.setMaxSize(800, 400);
        firstTable.setMinSize(800, 400);
        firstTable.setEditable(false);
        firstTable.setVisible(false);
        firstTable.setItems(getTableTada());

        TableColumn fio = new TableColumn();
        firstTable.getColumns().setAll(fio);
        fio.setText("ФИО");
        fio.setMinWidth(150);
        fio.setMaxWidth(150);

        fio.setCellValueFactory(new PropertyValueFactory<Student,String>("fio"));

        int numberOfLabs = 2;
        for (int i = 0; i < numberOfLabs; i++) {
            TableColumn laba = new TableColumn();
            laba.setCellValueFactory(new PropertyValueFactory("date"));
            int b=i+1;
            laba.setText("Лаб.№ "+ b);
            firstTable.getColumns().add(laba);
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

        forSem.getItems().addAll("1");
        forSem.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                chooseSem[0] = Integer.valueOf(t1);
                System.out.println(chooseSem[0]);
                forSub.getItems().addAll("Программирование", "АиСД");
            }
        });

        forSub.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                chooseSub[0] = t1;
                System.out.println(chooseSub[0]);
                forGroup.getItems().addAll("ИИИ-1-11");
            }
        });

        forGroup.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                chooseGroup[0] = t1;
                System.out.println(chooseGroup[0]);
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

    private ObservableList getTableTada()
    {
        List list = new ArrayList();
        Student studentResult = new Student();

        studentResult.setFio("Иванов Иван Иванович");
        studentResult.setDate("10-10-1000");

        list.add(studentResult);
        return FXCollections.observableList(list);

    }

    public class Student {

        Student()
        {
            fio = new SimpleStringProperty();
            date = new SimpleStringProperty();
        }

        private SimpleStringProperty fio;
        private SimpleStringProperty date;

        Student(StudentResult result,int labIndex)
        {
            this.fio = new SimpleStringProperty(result.surname + result.name + result.secondName);
            this.date = new SimpleStringProperty(result.dates.get(labIndex).day+"-"+result.dates.get(labIndex).month+"-"+result.dates.get(labIndex).year);
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

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }
    }

    public void downloadTests()
    {
        visibleField.getChildren().clear();
        VBox vBox = new VBox();

        HBox term = new HBox(10);
        HBox subject = new HBox(10);
        HBox number = new HBox(10);
        HBox variant = new HBox(10);

        TextField isTerm = new TextField();
        TextField isSubject = new TextField();
        TextField isNumber = new TextField();
        TextField isVariant = new TextField();

        

        term.getChildren().addAll();
        subject.getChildren().addAll();
        number.getChildren().addAll();
        variant.getChildren().addAll();
        vBox.getChildren().addAll(term, subject, number, variant);
        vBox.setAlignment(Pos.CENTER);
        visibleField.getChildren().add(vBox);
    }
}