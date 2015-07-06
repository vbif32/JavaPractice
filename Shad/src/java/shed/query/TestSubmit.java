package shed.query;

import shed.Queries;
import shed.Query;

import java.io.File;

/**
 * Created by Lognir on 04.07.2015.
 */
public class TestSubmit implements Query {

    public TestSubmit() {}

    public Integer id;
    public File labCode;
    public Boolean isCorrect;

    @Override
    public Queries getType() {
        return Queries.RESULT;
    }
}
