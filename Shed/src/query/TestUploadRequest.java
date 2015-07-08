package query;

import java.io.File;

/**
 * Класс для описания пользователя в системе
 * разграничивает доступ к функциям на сервере
 * и доступ к формам в пользовательском интерфейсе
 */
public class TestUploadRequest implements Query {

    public TestUploadRequest(){}
    public TestUploadRequest(File in, File out) {
        input = in;
        output = out;
    }

    public File input;   // входная последовательность
    public File output;  // выходная последовательность

    @Override
    public Queries getType() {
        return Queries.TESTUPLOAD;
    }
}