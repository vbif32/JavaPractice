package reply;

/**
 * Класс для описания ответа на запрос результатов теста
 */
public class TestResult implements Reply {

    public TestResult() {}
    public TestResult(int m) {
        mark = m;
    }

    public Integer mark;

    @Override
    public Replies getType() {
        return Replies.TESTRESULT;
    }
}
