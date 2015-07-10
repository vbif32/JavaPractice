package managers.DataBase;


import org.postgresql.util.PSQLException;
import transfer.LabSubmitDate;
import transfer.StudentResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * класс для вывода всей информации о студенте
 */
class ResultsHandler {
    private PreparedStatement stm;
    private Connection connection;
    private ResultSet res;
    private ResultSet res2;

    ResultsHandler(Connection connect) {
        connection = connect;
    }

    /**
     * Возвращает массив с информацией о студентах
     * @param GroupName - имя группы
     * @param Subject   - предмет
     * @param term      - семестр
     * @return массив с соответствующими данными
     * имя, фамилия, отчество, группа, идентификатор аккаунта, даты сдачи лабораторных для каждой лабораторной по порядку
     * Если лабораторная не сдана возвращает null
     */
    ArrayList<StudentResult> getResults(String GroupName, String Subject, int term) {
        StudentResult result;
        ArrayList<StudentResult> studentData;
        int subjectId = 0;
        int DefinedLabs = 0;
        try {
            //вывод идентификатор предмета
            stm = connection.prepareStatement("SELECT subject_id,number_of_labs FROM subject_table WHERE subject_name = ? AND term = ?");
            stm.setString(1, Subject);
            stm.setInt(2, term);
            res = stm.executeQuery();
            while (res.next()) {
                subjectId = res.getInt("subject_id");
                DefinedLabs = res.getInt("number_of_labs");
            }

            //вся информации о студенте
            String command = ("SELECT user_data.system_id,surname,name,second_name,is_lecturer,group_id FROM user_data");
            if (!GroupName.equals("All")) {
                int groupId = new GroupTableHandler(connection).getGroupId(GroupName);
                command = command + " WHERE group_id = ?";
                stm = connection.prepareStatement(command);
                stm.setInt(1, groupId);
            } else {
                stm = connection.prepareStatement(command);
            }
            res = stm.executeQuery();
            stm = connection.prepareStatement("SELECT * FROM student_results WHERE system_id = ? AND subject_id = ? AND term = ?");
            stm.setInt(2, subjectId);
            stm.setInt(3, term);
            studentData = new ArrayList<StudentResult>();
            try {
                while (res.next()) {
                    if (!res.getBoolean("is_lecturer")) {
                        result = new StudentResult(DefinedLabs);
                        stm.setInt(1, res.getInt("system_id"));
                        res2 = stm.executeQuery();
                        while (res2.next()) {
                            try {
                                res2.getInt("subject_id");
                                result.dates = new ArrayList<LabSubmitDate>();
                                for (int i = 4; i <= DefinedLabs + 3; i++) {
                                    try {
                                        result.dates.add((new LabSubmitDate(res2.getDate(i).toString())));
                                    } catch (NullPointerException e) {
                                        result.dates.add(null);
                                        //заполняет пустыми элементами если не может достать корректные данные
                                    }
                                }
                            } catch (PSQLException e) {
                                //если нет соедининия студент - предмет res2 будет пустой, выдаст ошибку
                            }
                            System.out.println("");
                            result.group = new GroupTableHandler(connection).getGroupName(res.getInt("group_id"));
                            result.name = res.getString("name");
                            result.surname = res.getString("surname");
                            result.secondName = res.getString("second_name");
                            studentData.add(result);
                            System.out.println("user added");
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                //если нет данных в БД для существующих требований
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //если сорвалось соединение
            return null;
        }
        return studentData;
    }

}
