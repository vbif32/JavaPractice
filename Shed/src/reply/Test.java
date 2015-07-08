package reply;

import java.io.File;

/**
 * Класс для описания теста в системе
 */
public class Test implements Reply {

    public Test() {}
    public Test(File in, File out) {
        input = in;
        output = out;
    }

    public File input;   // входная последовательность
    public File output;  // выходная последовательность

    @Override
    public Replies getType() {
        return Replies.TEST;
    }
}
