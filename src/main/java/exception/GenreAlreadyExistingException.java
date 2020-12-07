package exception;

public class GenreAlreadyExistingException extends Exception{
	private static final long serialVersionUID = 1L;

    public GenreAlreadyExistingException(String message)
    {
        super(message);
    }
}
