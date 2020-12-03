package exception;

public class UserAlreadyExistingException extends Exception{

    public UserAlreadyExistingException(String message){
        super(message);
    }
}
