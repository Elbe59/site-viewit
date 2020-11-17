package exception;

public class UserAlreadyExistingException extends Exception{

    public UserAlreadyExistingException(){
        super("Utilisateur déjà existant");
    }
}
