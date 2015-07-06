package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;

/**
 * Created by Lognir on 06.07.2015.
 */
public class RegisterResult implements QueryResult {
    public RegisterResult() {}

    public Boolean isRegistered;

    @Override
    public QueryResults getType() {
        return QueryResults.REGISTER;
    }
}
