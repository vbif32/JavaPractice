package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;

/**
 * Created by Lognir on 04.07.2015.
 */
public class Stats implements QueryResult {

    public Stats() {}

    // TODO: smth with anything (c) Somebody

    @Override
    public QueryResults getType() {
        return QueryResults.STATS;
    }
}
