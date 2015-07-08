package shed;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Lognir on 08.07.2015.
 */
public class StudentResult {

    public StudentResult() {
        dates = new ArrayList<>();
    }
    public StudentResult(int size) {
        dates = new ArrayList<>(size);
    }

    public String surname;  // Фамилия
    public String name;     // Имя
    public String secondName;// Отчество
    public String group; // Группа
    public ArrayList<Date> dates; //Даты сдачи лабораторных работ.

}
