package java.protocol;

//Ошибка, связанная с чтением или записью в файл. Размер, доступ, кривой путь, косяк с потоком на него.
public class FileReadingException extends Exception {
    public FileReadingException(String msg) {
        super(msg);
    }
}
