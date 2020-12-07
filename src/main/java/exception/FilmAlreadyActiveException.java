package exception;

public class FilmAlreadyActiveException extends Exception{
	private static final long serialVersionUID = 1L;

    public FilmAlreadyActiveException(String msg)
    {
        super(msg);
    }
}
