package managers.DataBase;

import org.postgresql.util.PSQLException;
import transfer.LabsPossible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class VariantTableHandler {

    private ResultSet res;
    private Connection connection;
    private PreparedStatement stm;
    private static String VarDataCommand = "SELECT * FROM lab_variants WHERE system_id = ? AND subject_id = ? AND term = ?";
    private static String NumberOfLabsCommand = "SELECT number_of_labs,term,subject_name FROM subject_table WHERE subject_id = ?";

    VariantTableHandler(Connection connect) {
        connection = connect;
    }


    ArrayList<LabsPossible> RetrieveVariantInfo(int userId, int SubjectId, ArrayList<LabsPossible> labs, boolean isTeacher) {
        ArrayList<LabsPossible> All = labs;
        String subjectName = "";
        try {
            stm = connection.prepareStatement(NumberOfLabsCommand);
            stm.setInt(1, SubjectId);
            res = stm.executeQuery();
            ArrayList<Integer> numberOfLabsBySemester = new ArrayList<>();
            ArrayList<Integer> semesterNumber = new ArrayList<>();
            if (res.next()) {
                subjectName = res.getString("subject_name");
                do {
                    numberOfLabsBySemester.add(res.getInt("number_of_labs"));
                    semesterNumber.add(res.getInt("term"));
                } while (res.next());
            } else {
                return labs;
            }
            stm = connection.prepareStatement(VarDataCommand);
            stm.setInt(1, userId);
            stm.setInt(2, SubjectId);
            int j = -1;
            for (int semester : semesterNumber) {
                j++;
                LabsPossible SubjectInfo = new LabsPossible();
                if (isTeacher) {
                    SubjectInfo.groups = new SubjectDataHandler(connection).GetGroupsStudying(SubjectId, semester);
                }
                SubjectInfo.variants = new ArrayList<>();
                stm.setInt(3, semester);
                res = stm.executeQuery();
                try {
                    while (res.next()) {
                        SubjectInfo.subject = subjectName;
                        SubjectInfo.term = semester;
                        for (int i = 4; i < numberOfLabsBySemester.get(j) + 4; i++) {
                            try {
                                if (res.getInt(i) == 0) {
                                    SubjectInfo.variants.add(0);//вариант не назначен
                                } else {
                                    SubjectInfo.variants.add(res.getInt(i));
                                }
                            } catch (PSQLException e) {
                                break;//неправильная информация в таблицы, вышли за пределы
                            }
                        }
                        All.add(SubjectInfo);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    return labs;//данных нет,либо не корректно введены
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
            return labs;//ошибка соединения
        }
        return All;
    }

}
