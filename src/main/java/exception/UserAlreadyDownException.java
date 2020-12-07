package exception;

public class UserAlreadyDownException extends Exception{
	private static final long serialVersionUID = 1L;
	
    public UserAlreadyDownException(String message)
    {
        super(message);
    }
}
