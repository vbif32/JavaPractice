package managers.DataBase;

import shed.QueryResult;
import shed.QueryResults;
import shed.queryResult.User;

import java.util.ArrayList;
import java.sql.Date;

//класс со всеми данными Студента(кроме log/pas) из БД (User + массив дат сдачи)
public class StudentResult extends User implements QueryResult {

    public ArrayList <Date> dates;//даты сдачи лабораторных (null = не сдана)

    public StudentResult(){
        dates=new ArrayList<Date>();
    }

    StudentResult(int size){
        dates=new ArrayList<Date>(size);
    }


    @Override
    public QueryResults getType() {
        return QueryResults.RESULT;
    }
}
