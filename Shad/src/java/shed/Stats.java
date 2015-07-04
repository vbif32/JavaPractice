package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class Stats implements QueryResult {

    public Stats() {}

    @Override
    public QueryResults getType() {
        return QueryResults.STATS;
    }
}
