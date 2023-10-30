public class TooManyThingsException extends RuntimeException {
    public TooManyThingsException() {
        super("Remove some old items to insert a new item");
    }
}
