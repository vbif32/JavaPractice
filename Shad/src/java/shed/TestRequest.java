package shed;
/**
 * Created by Lognir on 04.07.2015.
 */
public class TestRequest implements Query {

    public TestRequest() {}
    
    @Override
    public Queries getType() {
        return Queries.TEST;
    }
}
