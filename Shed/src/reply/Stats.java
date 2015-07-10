package reply;

import transfer.StudentResult;

import java.util.ArrayList;

/**
 * Класс для описания ответа на запрос статистики
 */
public class Stats implements Reply {

    public Stats() {
    }

    public Stats(ArrayList<StudentResult> al) {
        list = al;
    }

    public ArrayList<StudentResult> list;


    @Override
    public Replies getType() {
        return Replies.STATS;
    }
}
