package transfer;

import java.util.ArrayList;

/**
 * Класс описывающий семестр предмета
 */
public class LabsPossible {
    public LabsPossible() {
    }

    public String subject;                  //Предмет
    public Integer term;                    //Семестр (данного предмета)
    public ArrayList<Integer> variants;     //Список вариантов для каждой лабы index=лаба, value=варианты(кол-во)
    public ArrayList<String> groups;        //Соответствующие группы (для преподавателя)
}
