package shed;

import java.util.ArrayList;

/**
 * Created by Lognir on 08.07.2015.
 */
public class StudentResult {

    public StudentResult() {}
    public StudentResult(int size) {
        dates = new ArrayList<>(size);
    }

    public String surname;  // Фамилия
    public String name;     // Имя
    public String secondName;// Отчество
    public String group; // Группа
    public ArrayList<LabSubmitDate> dates; //Даты сдачи лабораторных работ.

}
