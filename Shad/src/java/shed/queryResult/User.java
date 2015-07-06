package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;

/**
 * Created by Dartaan on 01.07.2015.
 * Класс для описания пользователя в системе
 * разграничивает доступ к функциям на сервере
 * и доступ к формам в пользовательском интерфейсе
 */
public class User implements QueryResult {

    public User() {}

    public Integer id; // идентификатор доступа в системе
    public String surname;  // Фамилия
    public String name;     // Имя
    public String secondName;// Отчество
    public String group; // Группа
    public Boolean isLecturer; // флаг является ли преподавателем!

    @Override
    public QueryResults getType() {
        return QueryResults.USER;
    }
}
