package managers.DataBase;

import shed.queryResult.User;

import java.sql.*;

//класс для работы с личной информацией пользователя
class UserDataHandler {

    private PreparedStatement stm;
    private Connection connection;
    private ResultSet res;

    UserDataHandler(Connection connect){
        connection = connect;
    }

    //проверяет если данный пользователь существует

    boolean VerifyAccount(String login,String password){
        try{
            stm = connection.prepareStatement("SELECT password FROM user_data WHERE login = ?");
            stm.setString(1,login);
            res = stm.executeQuery();
            try{
                String Pass = " ";
                while(res.next()){
                    Pass = res.getString("password");
                }
                if(!Pass.equals(password)){
                    return false;
                }
            }catch(NullPointerException e){
                return false;
            }
        }catch(SQLException e){
            return false;
        }
        return true;
    }

    //добавляет аккаунт в БД

    boolean AddUser(User user,String Login,String password){
        try {
            stm = connection.prepareStatement("INSERT INTO user_data(login,password,surname,name,second_name,is_lecturer,group_name) VALUES(?, ?, ?, ?, ?, ?, ?)");
            stm.setString(1,Login);
            stm.setString(2, password);
            stm.setString(3,user.surname);
            stm.setString(4,user.name);
            stm.setString(5,user.secondName);
            stm.setBoolean(6, user.isLecturer);
            stm.setString(7,user.group);
            stm.executeUpdate();
        }catch(SQLException e){
            return false;
            }
        return true;
        }

}
