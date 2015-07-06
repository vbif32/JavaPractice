package shed.queryResult;

import shed.QueryResult;
import shed.QueryResults;

import java.io.File;

/**
 * Created by Dartaan on 01.07.2015.
 */
public class Test implements QueryResult {

    public Test() {}
    public Test(File in, File out) {
        input = in;
        output = out;
    }

    public File input;
    public File output;

    @Override
    public QueryResults getType() {
        return QueryResults.TEST;
    }
    //todo smth with this
}
