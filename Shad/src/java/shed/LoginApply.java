package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class LoginApply implements Query {

    public LoginApply() {}

    @Override
    public Queries getType() {
        return Queries.LOGIN;
    }
}
