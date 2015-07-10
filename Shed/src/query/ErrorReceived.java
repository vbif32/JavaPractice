package query;

/**
 * Класс для описания запроса, который информирует об ошибке при получении данных от сервера
 */
public class ErrorReceived implements Query {

    public ErrorReceived() {
    }

    public ErrorReceived(String msg) {
        message = msg;
    }

    public String message;      // Сообщение об ошибке

    @Override
    public Queries getType() {
        return Queries.ERROR;
    }
}
