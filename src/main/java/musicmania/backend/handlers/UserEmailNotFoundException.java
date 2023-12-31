package musicmania.backend.handlers;

public class UserEmailNotFoundException extends RuntimeException {
    public UserEmailNotFoundException(String message) {
        super(message);
    }
}