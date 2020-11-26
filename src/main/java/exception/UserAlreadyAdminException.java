package exception;

public class UserAlreadyAdminException extends Exception{
    public UserAlreadyAdminException()
    {
        super("Utilisateur déjà admin");
    }
}
