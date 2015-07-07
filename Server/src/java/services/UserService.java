package services;

import managers.DataBase.DatabaseManager;
import managers.DataBaseManager;
import shed.QueryResult;
import shed.query.LoginApply;
import shed.query.RegisterApply;
import shed.queryResult.QueryError;
import shed.queryResult.RegisterResult;
import shed.queryResult.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис по работе с пользователем
 */
public class UserService {
    /*
    регистрация пользователя
  */
    public static QueryResult registerUser(RegisterApply registerApply){
        String login=registerApply.login;
        String password=registerApply.password;
        String name=registerApply.name;
        String secondName=registerApply.secondName;
        String surname=registerApply.surname;
        String group=registerApply.group;
        boolean isLecturer=registerApply.isLecturer;
        String LOGIN_PASSWORD_PATTERN="^[а-яА-я]+$";
        Pattern pattern=Pattern.compile(LOGIN_PASSWORD_PATTERN);
        Matcher matcher=pattern.matcher(login);
        boolean log_val=matcher.matches();
        matcher=pattern.matcher(password);
        boolean pas_val=matcher.matches();
        if(pas_val && log_val==false){
            User user=new User();
            user.group=group;
            user.name=name;
            user.surname=surname;
            user.secondName=secondName;
            user.isLecturer=isLecturer;
            if(DatabaseManager.addUser(user, login, password)==true){
                RegisterResult r=new RegisterResult();
                r.isRegistered=true;
                return r;
            }
            else {
                QueryError error=new QueryError();
                error.message="Регистрация не удалась";
                return error;
            }
        }
        else {
            QueryError error=new QueryError();
            error.message="Некорректный логин и/или пароль";
            return error;
        }
    }
    /*
    авторизация пользователя
     */
    public static QueryResult authenticateUser(LoginApply loginApply){
        String login=loginApply.login;
        String password=loginApply.password;
        String LOGIN_PASSWORD_PATTERN="^[а-яА-я]+$";
        Pattern pattern=Pattern.compile(LOGIN_PASSWORD_PATTERN);
        Matcher matcher=pattern.matcher(login);
        boolean log_val=matcher.matches();
        matcher=pattern.matcher(password);
        boolean pas_val=matcher.matches();
        if(pas_val && log_val==false){
            if(DatabaseManager.verifyAccount(login,password)==true){
                /*тут что-то должно быть*/
                return new User();
            }
            else{
                QueryError error=new QueryError();
                error.message="Авторизация не удалась";
                return error;
            }
        }
        else{
            QueryError error=new QueryError();
            error.message="Некорректный логин и/или пароль";
            return error;
        }
    }
}
