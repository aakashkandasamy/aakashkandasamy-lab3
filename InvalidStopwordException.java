public class InvalidStopwordException extends Exception {
    public InvalidStopwordException(String message) {
        super("InvalidStopwordException: " + message);
    }
}