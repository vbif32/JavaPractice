package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class QueryError implements QueryResult {

    public QueryError() {}

    @Override
    public QueryResults getType() {
        return QueryResults.ERROR;
    }
}
