package exception;

public class FilmAlreadyActiveException extends Exception{

    public FilmAlreadyActiveException()
    {
        super("Le Film est déjà activé");
    }
}
