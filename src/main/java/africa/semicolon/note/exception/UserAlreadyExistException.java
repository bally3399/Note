package africa.semicolon.note.exception;

public class UserAlreadyExistException extends NoteAppException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
