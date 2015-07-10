package reply;

/**
 * Класс для описания ответа на неправильный запрос
 */
public class QueryError implements Reply {

    public QueryError() {
    }

    public QueryError(String msg) {
        message = msg;
    }

    public String message;      // сообщение об ошибке

    @Override
    public Replies getType() {
        return Replies.ERROR;
    }
}
