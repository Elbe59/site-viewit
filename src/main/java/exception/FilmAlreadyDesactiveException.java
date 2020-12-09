package exception;

public class FilmAlreadyDesactiveException extends Exception{
	private static final long serialVersionUID = 1L;

    public FilmAlreadyDesactiveException(String message){
        super(message);
    }
}
