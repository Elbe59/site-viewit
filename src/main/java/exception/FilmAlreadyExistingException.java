package exception;

public class FilmAlreadyExistingException extends Exception{

    public FilmAlreadyExistingException(String message)
    {
        super(message);
    }
}
