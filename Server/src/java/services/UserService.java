package services;

import shed.QueryResult;
import shed.query.LoginApply;
import shed.query.RegisterApply;
import shed.queryResult.RegisterResult;
import shed.queryResult.User;

/**
 * Сервис по работе с пользователем
 */
public class UserService {
    /*
    регистрация пользователя
    TODO: Написать тело функций registerUser() и AuthenticateUser()
     */
    public static QueryResult registerUser(RegisterApply registerApply){
        return new RegisterResult();
    }
    /*
    авторизация пользователя
     */
    public static QueryResult authenticateUser(LoginApply loginApply){
        return new User();
    }
    /*
    Получение списка студентов в группе
     */
    public boolean getStudents(){
        return false;
    }
}
