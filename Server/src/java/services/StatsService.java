package services;

import managers.DataBase.DatabaseManager;
import shed.QueryResult;
import shed.query.StatsRequest;
import shed.queryResult.Stats;

/**
 * Created by ������������ on 06.07.2015.
 */
public class StatsService {

    /**
     * ������ � ��������� �� �� ������� � ��������� � ��������� �����������: ������� � ������� ������
     * ������� ArrayList ���������
     */
    public static QueryResult getStats(StatsRequest statsRequest) {
        DatabaseManager manager = new DatabaseManager();
        return new Stats(manager.GetStudentInfo(statsRequest.group, statsRequest.subject));
    }
}
