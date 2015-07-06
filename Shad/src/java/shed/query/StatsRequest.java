package shed.query;

import shed.Queries;
import shed.Query;

/**
 * Created by Lognir on 04.07.2015.
 */
public class StatsRequest implements Query {

    public StatsRequest() {}

    public Integer id;
    public String subject;
    public Integer term;
    public String group;

    @Override
    public Queries getType() {
        return Queries.STATS;
    }
}
