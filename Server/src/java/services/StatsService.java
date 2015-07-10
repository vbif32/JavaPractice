package services;

import connect.MainServer;
import query.StatsRequest;
import reply.Reply;
import reply.Stats;

/**
 * Класс сервиса статистики
 * Ответственные:
 * Кирилл Павлов
 * Левон Антонян
 */
public class StatsService {

    /**
     * Запрос к менеджеру БД за данными о студентах
     *
     * @param statsRequest
     * @return
     */
    public static Reply getStats(StatsRequest statsRequest) {
        return new Stats(MainServer.dbManager.GetStudentInfo(statsRequest.group, statsRequest.subject, statsRequest.term));
    }
}
