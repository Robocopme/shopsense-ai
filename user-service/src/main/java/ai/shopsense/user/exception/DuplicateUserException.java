package ai.shopsense.user.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String email) {
        super("User already exists for email " + email);
    }
}
