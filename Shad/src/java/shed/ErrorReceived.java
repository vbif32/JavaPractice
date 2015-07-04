package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class ErrorReceived implements Query {

    public ErrorReceived() {}

    @Override
    public Queries getType() {
        return Queries.ERROR;
    }
}
