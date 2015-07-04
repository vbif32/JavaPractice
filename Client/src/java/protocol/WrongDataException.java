package protocol;

//Финальная ошибка в передаваемых данных. Для клиента есть возможность получить от сервера репорт.
public class WrongDataException extends Exception {
    public WrongDataException(String msg) {
        super(msg);
    }
}
