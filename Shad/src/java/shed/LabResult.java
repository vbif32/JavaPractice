package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class LabResult implements QueryResult {

    public LabResult() {}

    @Override
    public QueryResults getType() {
        return QueryResults.RESULT;
    }
}
