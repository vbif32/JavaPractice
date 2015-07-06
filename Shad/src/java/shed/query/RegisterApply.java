package shed.query;

import shed.Queries;
import shed.Query;

/**
 * Created by Lognir on 04.07.2015.
 */
public class RegisterApply implements Query {

    public RegisterApply() {}

    public String login;
    public String password;
    public String name;
    public String surname;
    public String secondName;
    public String group;
    public Boolean isLecturer;

    @Override
    public Queries getType() {
        return Queries.REGISTRATION;
    }
}
