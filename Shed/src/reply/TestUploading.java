package reply;

/**
 * Created by Dartaan on 08.07.2015.
 */
public class TestUploading implements Reply {

    public TestUploading() {
    }

    public TestUploading(Boolean flag) {
        isSuccess = flag;
    }

    public Boolean isSuccess;

    @Override
    public Replies getType() {
        return Replies.TESTUPLOADING;
    }
}
