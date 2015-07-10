package reply;

/**
 * Класс для описания ответа на запрос результатов теста
 */
public class TestResult implements Reply {

    public TestResult() {
    }

    public TestResult(Boolean flag) {
        isSuccess = flag;
    }

    public Boolean isSuccess; // флаг успешности отправки результатов

    @Override
    public Replies getType() {
        return Replies.TESTRESULT;
    }
}
