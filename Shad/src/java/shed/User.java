package shed;

/**
 * Created by Dartaan on 01.07.2015.
 * Класс для описания пользователя в системе
 * разграничивает доступ к функциям на сервере
 * и доступ к формам в пользовательском интерфейсе
 */
public class User {
    public int id; // идентификатор доступа в системе
    public String surname;  // Фамилия
    public String name;     // Имя
    public String secondName;// Отчество
    public boolean isLecturer; // флаг является ли преподавателем!
}
