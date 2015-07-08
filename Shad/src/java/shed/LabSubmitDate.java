package shed;

/**
 * Created by Lognir on 08.07.2015.
 */
public class LabSubmitDate {

    //Класс для хранения даты сдачи лаб

    public LabSubmitDate() {}

    //Конструктор по строке вида yyyy-mm-dd, т.е. того, который возвращается от (sql.date).toString() и т.д.
    public LabSubmitDate(String s) {
        String[] split = s.split("-");
        year = new Integer(split[0]);
        month = new Integer(split[1]);
        day = new Integer(split[2]);
    }

    public Integer day;
    public Integer month;
    public Integer year;

}
