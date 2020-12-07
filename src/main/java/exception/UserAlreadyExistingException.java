package exception;

public class UserAlreadyExistingException extends Exception{
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistingException(String message){
        super(message);
    }
}
