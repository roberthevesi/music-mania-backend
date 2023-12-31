package musicmania.backend.handlers;

public class UsedVerificationCodeException extends RuntimeException {
    public UsedVerificationCodeException(String message) {
        super(message);
    }
}
