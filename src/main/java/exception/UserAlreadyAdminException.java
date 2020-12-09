package exception;

public class UserAlreadyAdminException extends Exception{
	private static final long serialVersionUID = 1L;
	
    public UserAlreadyAdminException(String message){
        super(message);
    }
}
