package shed.query;

import shed.Queries;
import shed.Query;

import java.io.File;

/**
 * Created by Lognir on 04.07.2015.
 */
public class TestRequest implements Query {

    public TestRequest() {}

    public Integer userId;
    public String subject;
    public Integer term;
    public Integer labNumber;
    
    @Override
    public Queries getType() {
        return Queries.TEST;
    }
}
