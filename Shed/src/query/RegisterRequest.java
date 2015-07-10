package query;

/**
 * Класс для описания запроса на регистрацию в системе
 */
public class RegisterRequest implements Query {

    public RegisterRequest() {
    }

    public String login;        // логин
    public String password;     // пароль
    public String surname;      // фамилия
    public String name;         // имя
    public String secondName;   // отчество
    public String group;        // группа
    public Boolean isLecturer;  // флаг является ли преподавателем!

    @Override
    public Queries getType() {
        return Queries.REGISTRATION;
    }
}
