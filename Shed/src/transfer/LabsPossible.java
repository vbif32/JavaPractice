package transfer;

import java.util.ArrayList;

/**
 * Created by Lognir on 09.07.2015.
 */

//Данным классом предполагается описывать семестр предмета
public class LabsPossible {
    public LabsPossible() {}

    public String subject;      //Предмет
    public Integer term;        //Семестр (данного предмета)
    public ArrayList<Integer> variants;     //Список вариантов для каждой лабы index=лаба, value=варианты(кол-во)
}
