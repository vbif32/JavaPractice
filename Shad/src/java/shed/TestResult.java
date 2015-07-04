package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class TestResult implements Query {

    public TestResult() {}

    @Override
    public Queries getType() {
        return Queries.RESULT;
    }
}
