package sorokin.java.course.exception;

public class EntityNotFoundException extends IllegalArgumentException {

    public EntityNotFoundException(String s) {
        super(s);
    }
}
