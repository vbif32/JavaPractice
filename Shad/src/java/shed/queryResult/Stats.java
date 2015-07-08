package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;
import shed.StudentResult;

import java.util.ArrayList;

/**
 * Created by Lognir on 04.07.2015.
 */
public class Stats implements QueryResult {

    public Stats() {
        list = new ArrayList<>();
    }
    public Stats(ArrayList<StudentResult> al) {
        list = al;
    }

    public ArrayList<StudentResult> list;

    @Override
    public QueryResults getType() {
        return QueryResults.STATS;
    }
}
