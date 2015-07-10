package managers.DataBase;

import reply.User;
import java.sql.*;
import java.util.ArrayList;

/**
 * класс для работы с личной информацией пользователя
 */
class UserDataHandler {

    private PreparedStatement stm;
    private Connection connection;
    private ResultSet res;

    UserDataHandler(Connection connect) {
        connection = connect;
    }

    /**
     * проверяет существует ли данный пользователь
     * @param login
     * @param password
     * @return
     */
    int VerifyAccount(String login, String password) {
        try {
            stm = connection.prepareStatement("SELECT password,system_id FROM user_data WHERE login = ?");
            stm.setString(1, login);
            res = stm.executeQuery();
            while (res.next()) {
                String pass = res.getString("password");
                if (pass.equals(password)) {
                    return res.getInt("system_id");
                }
                return 0;
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    /**
     * возвращает данные пользователя
     * @param systemId
     * @return
     */
    User getPersonalData(int systemId) {
        User user = new User();
        try {
            stm = connection.prepareStatement("SELECT * FROM user_data WHERE system_id = ?");
            stm.setInt(1, systemId);
            res = stm.executeQuery();
            res.next();
            user.id = res.getInt("system_id");
            user.isLecturer = res.getBoolean("is_lecturer");
            if (user.isLecturer) {
                user.group = null;
            } else {
                user.group = new GroupTableHandler(connection).getGroupName(res.getInt("group_id"));
            }
            user.surname = res.getString("surname");
            user.name = res.getString("name");
            user.secondName = res.getString("second_name");
            try {
                Array ar = res.getArray("studied_subjects");
                user.labInfo = new ArrayList<>();
                Integer[] subjects = (Integer[]) ar.getArray();
                VariantTableHandler handler = new VariantTableHandler(connection);
                for (int subject : subjects) {
                    user.labInfo = handler.RetrieveVariantInfo(user.id, subject, user.labInfo, user.isLecturer);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                user.labInfo = null;
                //данных нет, выходим возвращаем пользователя без info о вариантах
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }


    /**
     * добавляет аккаунт в БД
     * @param user
     * @param login
     * @param password
     * @return
     */
    boolean AddUser(User user, String login, String password) {
        try {
            if (!user.isLecturer && !new GroupTableHandler(connection).checkCorrectness(user.group)) {
                return false;
            }
            stm = connection.prepareStatement("INSERT INTO user_data(login,password,surname,name,second_name,is_lecturer,group_name) VALUES(?, ?, ?, ?, ?, ?, ?)");
            stm.setString(1, login);
            stm.setString(2, password);
            stm.setString(3, user.surname);
            stm.setString(4, user.name);
            stm.setString(5, user.secondName);
            stm.setBoolean(6, user.isLecturer);
            stm.setInt(7, new GroupTableHandler(connection).getGroupId(user.group));
            stm.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

}
