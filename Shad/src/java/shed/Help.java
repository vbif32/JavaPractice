package shed;

/**
 * Created by Lognir on 04.07.2015.
 */
public class Help implements QueryResult {

    public Help() {}

    @Override
    public QueryResults getType() {
        return QueryResults.HELP;
    }
}
