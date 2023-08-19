package movierecommendation.exception;

public class MovieAlreadyExistsException extends Throwable {
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}