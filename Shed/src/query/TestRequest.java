package query;

/**
 * Класс для описания пользователя в системе
 * разграничивает доступ к функциям на сервере
 * и доступ к формам в пользовательском интерфейсе
 */
public class TestRequest implements Query {

    public TestRequest() {
    }

    public Integer id;          // идентификатор доступа в системе
    public String subject;      // предмет
    public Integer term;        // семестр
    public Integer labNumber;   // номер лабораторной работы
    public Integer variant;     // вариант лабороторной работы


    @Override
    public Queries getType() {
        return Queries.TESTDOWNLOAD;
    }
}
