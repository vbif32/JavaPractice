package protocol;

//Клиентская ошибка в данных, присланных сервером. Без репорта.
public class ServerBadDataException extends Exception {
    public ServerBadDataException(String msg) {
        super(msg);
    }
}
