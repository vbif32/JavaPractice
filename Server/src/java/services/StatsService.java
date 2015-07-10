package services;

import connect.MainServer;
import managers.DataBase.DatabaseManager;
import query.Query;
import query.StatsRequest;
import reply.Reply;
import reply.Stats;


/**
 * Created by Пользователь on 06.07.2015.
 */
public class StatsService {

    /**
     * Запрос к менеджеру БД за данными о студентах с входящими параметрами: предмет и учебная группа
     * Возврат ArrayList студентов
     */
    public static Reply getStats(StatsRequest statsRequest) {
        return new Stats(MainServer.dbManager.GetStudentInfo(statsRequest.group, statsRequest.subject, statsRequest.term));
    }
}
