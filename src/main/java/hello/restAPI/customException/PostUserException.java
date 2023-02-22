package hello.restAPI.customException;

public class PostUserException extends RuntimeException {
    PostUserException() {

    }

    public PostUserException(String message) {
        super(message);
    }
}
