package exception;

public class GenreNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

    public GenreNotFoundException(String message)
    {
        super(message);
    }
}
