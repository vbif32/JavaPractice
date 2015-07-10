package transfer;

import java.util.ArrayList;

/**
 * Класс для хранения успеваемости студента
 */
public class StudentResult {

    public StudentResult() {
    }

    public StudentResult(int size) {
        dates = new ArrayList<>(size);
    }

    public String surname;                  // Фамилия
    public String name;                     // Имя
    public String secondName;               // Отчество
    public String group;                    // Группа
    public ArrayList<LabSubmitDate> dates; //Даты сдачи лабораторных работ.

}
