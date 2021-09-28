package ph.easyaf.batchobjects;

public class BatchObjectException extends Exception {

    public BatchObjectException(String errorMessage) {
        super(errorMessage);
    }

    public BatchObjectException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
