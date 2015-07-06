package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;

/**
 * Created by Lognir on 04.07.2015.
 */
public class QueryError implements QueryResult {

    public QueryError() {}
    public QueryError(String msg) {
        message = msg;
    }

    public String message;

    @Override
    public QueryResults getType() {
        return QueryResults.ERROR;
    }
}
