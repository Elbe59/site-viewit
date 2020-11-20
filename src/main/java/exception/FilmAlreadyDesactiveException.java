package exception;

public class FilmAlreadyDesactiveException extends Exception{

    public FilmAlreadyDesactiveException()
    {
        super("Le film est déjà désactivé");
    }
}
