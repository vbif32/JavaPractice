package reply;

/**
 * Класс для описания ответа на запрос регистрации
 */
public class Registration implements Reply {

    public Registration() {}
    public Registration(Boolean flag){
        isSuccess = flag;
    }

    public Boolean isSuccess;   // флаг успешности регистрации

    @Override
    public Replies getType() {
        return Replies.REGISTER;
    }
}
