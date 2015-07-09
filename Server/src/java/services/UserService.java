package services;

import managers.DataBase.DatabaseManager;
import query.LoginRequest;
import query.RegisterRequest;
import reply.QueryError;
import reply.Registration;
import reply.Reply;
import reply.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис по работе с пользователем
 */
public class UserService {
    /*
    регистрация пользователя
  */
    public static Reply registerUser(RegisterRequest registerApply){
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
                Registration r=new Registration();
                r.isSuccess=true;
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
    public static Reply authenticateUser(LoginRequest loginApply){
        String login=loginApply.login;
        String password=loginApply.password;
        String LOGIN_PASSWORD_PATTERN="^[а-яА-я]+$";
        Pattern pattern=Pattern.compile(LOGIN_PASSWORD_PATTERN);
        Matcher matcher=pattern.matcher(login);
        boolean log_val=matcher.matches();
        matcher=pattern.matcher(password);
        boolean pas_val=matcher.matches();
        if(pas_val && log_val==false){
            return DatabaseManager.verifyAccount(login,password);
        }
        else{
            QueryError error=new QueryError();
            error.message="Некорректный логин и/или пароль";
            return error;
        }
    }
}
