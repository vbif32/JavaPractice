package query;

/**
 * Класс для описания запроса на авторизацию пользователя в системе
 */
public class LoginRequest implements Query {

    public LoginRequest() {
    }

    public LoginRequest(String lgn, String pass) {
        login = lgn;
        password = pass;
    }

    public String login;        // логин
    public String password;     // пароль

    @Override
    public Queries getType() {
        return Queries.LOGIN;
    }
}
