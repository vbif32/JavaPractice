package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;

/**
 * Created by Lognir on 04.07.2015.
 */
public class LabResult implements QueryResult {

    public LabResult() {}
    public LabResult(int m) {
        mark = m;
    }

    public Integer mark;

    @Override
    public QueryResults getType() {
        return QueryResults.RESULT;
    }
}
