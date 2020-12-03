package exception;

public class UserAlreadyDownException extends Exception{
    public UserAlreadyDownException(String message)
    {
        super(message);
    }
}
