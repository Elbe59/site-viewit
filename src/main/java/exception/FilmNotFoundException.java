package exception;

public class FilmNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;

    public FilmNotFoundException(String message)
    {
        super(message);
    }
}
