package reply;

/**
 * Класс для описания пользователя в системе и ответа на запрос авторизации
 * разграничивает доступ к функциям на сервере
 * и доступ к формам в пользовательском интерфейсе
 */
public class User implements Reply {

    public User() {}

    public Integer id;          // идентификатор доступа в системе
    public String surname;      // Фамилия
    public String name;         // Имя
    public String secondName;   // Отчество
    public String group;        // Группа
    public Boolean isLecturer;  // флаг является ли преподавателем!

    @Override
    public Replies getType() {
        return Replies.USER;
    }
}
