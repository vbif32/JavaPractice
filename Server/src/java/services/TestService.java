package services;

import shed.QueryResult;
import shed.query.TestRequest;
import shed.query.TestSubmit;
import shed.queryResult.LabResult;
import shed.queryResult.Test;

/**
 * Created by ������������ on 06.07.2015.
 */
public class TestService {

    //TODO: �������� ���� ��� �������.


    //���������� ����� �� �������
    public static QueryResult getTest(TestRequest testRequest) {return  new Test();}

    //��������� ���������� ������ �� ������� � ���������� ���������� � ����
    public static QueryResult submitTest(TestSubmit testSubmit) {return  new LabResult();}
}
