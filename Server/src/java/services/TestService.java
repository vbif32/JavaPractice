package services;

import shed.QueryResult;
import shed.query.TestRequest;
import shed.query.TestSubmit;
import shed.queryResult.LabResult;
import shed.queryResult.Test;

/**
 * Created by Пользователь on 06.07.2015.
 */
public class TestService {

    //TODO: Написать тела для функций.


    //Возвращает тесты по запросу
    public static QueryResult getTest(TestRequest testRequest) {return  new Test();}

    //Принимает результаты тестов на сервере и возвращает результаты о лабе
    public static QueryResult submitTest(TestSubmit testSubmit) {return  new LabResult();}
}
