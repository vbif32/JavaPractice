package managers.DataBase;



import java.sql.*;
import java.util.*;

//класс для вывода всей информации о студенте

class ResultsHandler {

    private PreparedStatement stm;
    private Connection connection;
    private ResultSet res;
    private ResultSet res2;

    ResultsHandler(Connection connect){
        connection = connect;
    }

    /*принимает на вход имя группы и имя предмета.
      выводит массив с информацией о студентах соответствующим данным
      имя, фамилия, отчество, группа, идентификатор аккаунта, даты сдачи лабораторных для каждой лабораторной по порядку
      Если лабораторная не сдана возвращает null
     */

    ArrayList<StudentResult> getResults(String GroupName,String Subject){
        StudentResult result;
        ArrayList<StudentResult> studentData;
        try{
             //вывод идентификатор предмета
            stm = connection.prepareStatement("SELECT subject_id,number_of_labs FROM subject_table WHERE subject_name = ?");
            stm.setString(1,Subject);
            res = stm.executeQuery();
            res.next();
            int subjectId = res.getInt("subject_id");
            int DefinedLabs = res.getInt("number_of_labs");
            //вся информации о студенте
            String command =("SELECT user_data.system_id,surname,name,second_name,is_lecturer,group_name FROM user_data");
            if(!GroupName.equals("All")){
                command =command+" WHERE group_name = ?";
                stm =connection.prepareStatement(command);
                stm.setString(1,GroupName);
            }
            else{stm =connection.prepareStatement(command);}
            res=stm.executeQuery();
            stm =connection.prepareStatement("SELECT * FROM student_results WHERE system_id = ? AND subject_id = ?");
            stm.setInt(2,subjectId);
            studentData= new ArrayList<StudentResult>();
            try{
                while (res.next()){
                    result=new StudentResult(DefinedLabs);
                    result.id = res.getInt("system_id");
                    stm.setInt(1,result.id);
                    res2 = stm.executeQuery();
                    res2.next();
                    try{res2.getInt("subject_id");
                        for(int i=3;i<=DefinedLabs+2;i++){
                            try{
                                result.dates.add(res2.getDate(i));
                            }catch(Exception e){
                                result.dates.add(null);//заполняет пустыми элементами если не может достать корректные данные
                            }
                        }
                        result.group = res.getString("group_name");
                        result.name = res.getString("name");
                        result.surname = res.getString("surname");
                        result.secondName = res.getString("second_name");
                        result.isLecturer = res.getBoolean("is_lecturer");
                        studentData.add(result);
                    }catch (Exception e){
                        //если нет соедининия студент - предмет res2 будет пустой, выдаст ошибку
                    }
                }
            }catch(NullPointerException e){
                //если нет данных в БД для существующих требований
            }
        }catch(SQLException e){
            //если сорвалось соединение
            return null;
            }
        return studentData;
    }


}
