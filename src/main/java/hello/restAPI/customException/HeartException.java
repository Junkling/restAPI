package hello.restAPI.customException;

public class HeartException extends RuntimeException{
    public HeartException() {
    }

    public HeartException(String message) {
        super(message);
    }
}
