package exception;

public class UserAlreadyDownException extends Exception{
    public UserAlreadyDownException()
    {
        super("Utilisateur est déjà non-admin");
    }
}
