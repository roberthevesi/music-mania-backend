package musicmania.backend.handlers;

public class IncorrectVerificationCodeException extends RuntimeException {
    public IncorrectVerificationCodeException(String message) {
        super(message);
    }
}
