package query;

/**
 * Класс для описания запроса статистики успеваемости
 */
public class StatsRequest implements Query {

    public StatsRequest() {}

    public Integer id;      //  идентификатор доступа в системе
    public String subject;  //  предмет
    public Integer term;    //  семестр
    public String group;    //  группа

    @Override
    public Queries getType() {
        return Queries.STATS;
    }
}
