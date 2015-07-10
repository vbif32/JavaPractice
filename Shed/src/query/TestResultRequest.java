package query;

import java.io.File;

/**
 * Класс для описания запроса отправки результатов прохождения теста
 */
public class TestResultRequest implements Query {

    public TestResultRequest() {}

    public Integer id;          // идентификатор доступа в системе
    public Boolean isCorrect;   // флаг на соответствие тесту
    public Integer subject_id;  // идентификатор предмета
    public Integer term;        // семестр
    public Integer labNumber;   // номер лабораторной работы

    @Override
    public Queries getType() {
        return Queries.TESTRESULT;
    }
}
