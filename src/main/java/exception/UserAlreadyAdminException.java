package exception;

public class UserAlreadyAdminException extends Exception{
    public UserAlreadyAdminException(String message)
    {
        super(message);
    }
}
