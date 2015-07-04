package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class RegisterApply implements Query {

    public RegisterApply() {}

    @Override
    public Queries getType() {
        return Queries.REGISTRATION;
    }
}
