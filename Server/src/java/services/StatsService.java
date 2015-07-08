package services;

import managers.DataBase.DatabaseManager;
import shed.QueryResult;
import shed.query.StatsRequest;
import shed.queryResult.Stats;

/**
 * Created by Пользователь on 06.07.2015.
 */
public class StatsService {

    /**
     * Запрос к менеджеру БД за данными о студентах с входящими параметрами: предмет и учебная группа
     * Возврат ArrayList студентов
     */
    public static QueryResult getStats(StatsRequest statsRequest) {
        DatabaseManager manager = new DatabaseManager();
        return new Stats(manager.GetStudentInfo(statsRequest.group, statsRequest.subject));
    }
}
