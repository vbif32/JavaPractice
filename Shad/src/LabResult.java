/**
 * Created by Lognir on 04.07.2015.
 */
public class LabResult implements Query {

    public LabResult() {}

    @Override
    public Queries getType() {
        return Queries.RESULT;
    }
}
