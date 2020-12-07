package exception;

public class FilmAlreadyExistingException extends Exception{
	private static final long serialVersionUID = 1L;

    public FilmAlreadyExistingException(String message)
    {
        super(message);
    }
}
