package services;

import shed.QueryResult;
import shed.query.TestRequest;
import shed.query.TestSubmit;
import shed.queryResult.LabResult;
import shed.queryResult.Test;

/**
 */
public class TestService {

    //TODO: Написать тела функций.


    //Возвращает тесты для лаб
    public static QueryResult getTest(TestRequest testRequest) {return  new Test();}

    //Вовзращает результаты студента по тестам
    public static QueryResult submitTest(TestSubmit testSubmit) {return  new LabResult();}
}
