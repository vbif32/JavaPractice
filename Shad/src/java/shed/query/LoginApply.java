package shed.query;

import shed.Queries;
import shed.Query;

/**
 * Created by Lognir on 04.07.2015.
 */
public class LoginApply implements Query {

    public LoginApply() {}
    public LoginApply(String login, String pass) {
        this.login = login;
        password = pass;
    }

    public String login;
    public String password;

    @Override
    public Queries getType() {
        return Queries.LOGIN;
    }
}
