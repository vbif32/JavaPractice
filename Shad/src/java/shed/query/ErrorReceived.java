package shed.query;

import shed.Queries;
import shed.Query;

/**
 * Created by Lognir on 04.07.2015.
 */
public class ErrorReceived implements Query {

    public ErrorReceived() {}
    public ErrorReceived(String msg) {}

    public String message;

    @Override
    public Queries getType() {
        return Queries.ERROR;
    }
}
