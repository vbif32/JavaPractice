/**
 * Created by Lognir on 04.07.2015.
 */
public class HelpRequest implements Query {

    public HelpRequest() {}

    @Override
    public Queries getType() {
        return Queries.HELP;
    }
}
