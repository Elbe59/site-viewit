package exception;

public class FilmAlreadyExistingException extends Exception{

    public FilmAlreadyExistingException()
    {
        super("Le film existe déjà");
    }
}
