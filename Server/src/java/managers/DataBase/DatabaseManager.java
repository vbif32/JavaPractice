package managers.DataBase;

import reply.User;
import transfer.StudentResult;

import java.sql.*;
import java.util.*;

//<editor-fold desc="DataBase Description">
/*сделанно под базу данных с данной структурой:
таблица user_data - для хранения личной информации
    столбцы:
    system_id - INT auto_increment unique primary key not null
    login - VARCHAR(128) unique not null
    password - VARCHAR(128) not null
    surname - VARCHAR(128) not null
    name - VARCHAR(128) not null
    second_name - VARCHAR(128) not null
    is_lecturer - BOOL not null
    group_name - VARCHAR(128) not null

таблица subject_table для хранения информации о предмете
    столбцы:
    subject_id - INT primary key unique not null
    subject_name - VARCHAR(128) not null
    number_of_labs - INT not null
    groups_studying - INT[]

таблица lab_variants для хранения вариантов для студентов
    столбцы;
     system_id - INT primary key not NULL
     subject_id - INT primary key not NULL
     term - INT primary key not NULL
     lab_1 ....... аналогично student results
таблица student_results даты сдачи студентом лабораторных по предмету
    столбцы:
    system_id - INT primary key not null
    subject_id - INT primary key not null
    //базовая структура содержит 6 лаб, возможно добаления и удаления столбцов лабораторных
    lab_1 - DATE, lab_2 - DATE ...........
таблица group_data хранит информацию о группах
    столбцы:
    group_id - INT Primary key
    group_name - VARCHAR(128)
таблица var_lab_table хранит информацию о возможных вариантах в лабораторных
    столбцы:
    subject_id - INT Primary key not NULL
    term - INT primary key NOT NULL
    lab_1 аналогично.......
 */
//</editor-fold>

/*класс для работы с базой данных
имеет функии - проверка аккаунта,регистрация пользователя,вывод информации о студенте по группе и предмету,вывод информации про всех студентов
 */

public class DatabaseManager {
    //информация для подключения к БД
    private static final String DatabaseType = "postgresql";
    private static final String DatabaseName = "ProgrammingDB";
    private static final String DatabaseHost = "localhost:5432";
    private static final String URL = ("jdbc:"+DatabaseType+"://"+DatabaseHost+"/"+DatabaseName);
    private static final String SuperUser = "postgres";
    private static final String Password = "root";
    private static Connection DatabaseConnection=null;
    private  PreparedStatement stm;
    private ResultSet res;

    public DatabaseManager(){
        ConnectToDB();
    }

    private void ConnectToDB(){
        try{
            DatabaseConnection = DriverManager.getConnection(URL, SuperUser, Password);
        }catch(SQLException e){
            System.out.println("cant`t connect");
        }
    }
    //функция проверки аккаунта
    public static User verifyAccount(String login, String password){
        UserDataHandler temp = new UserDataHandler(DatabaseConnection);
        int id =temp.VerifyAccount(login,password);
        return temp.getPersonalData(id);
    }

    //функция вноса данных пользователя в БД
    public static boolean addUser(User user, String login, String password){
        return(new UserDataHandler(DatabaseConnection).AddUser(user,login,password));
    }

    //функция вывода информации студентов по критериям
    public ArrayList<StudentResult> GetStudentInfo(String GroupName,String Subject,int term){
        return(new ResultsHandler(DatabaseConnection).getResults(GroupName,Subject,term));
    }

    //функция для вывода всех данных в виде map<предмет,список студентов<все данные студента>>
    public Map<String,ArrayList<StudentResult>> getData(){
        Map<String,ArrayList<StudentResult>> map = new TreeMap<String, ArrayList<StudentResult>>();
        try {
            stm = DatabaseConnection.prepareStatement("SELECT subject_name,term FROM subject_table");
            res = stm.executeQuery();
            ArrayList<String> arr=new ArrayList<String>();
            while(res.next()){
                arr.add(res.getString("subject_name")+"term "+res.getInt("term"));
            }
            for(String subject:arr){
                int term = res.getInt("term");
                map.put(subject,this.GetStudentInfo("All",subject,term));
            }
        }catch(SQLException e){
            return null;//can`t connect
        }
        return map;
    }


}
