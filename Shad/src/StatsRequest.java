/**
 * Created by Lognir on 04.07.2015.
 */
public class StatsRequest implements Query {

    public StatsRequest() {}

    @Override
    public Queries getType() {
        return Queries.STATS;
    }
}
