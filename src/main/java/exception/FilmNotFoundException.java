package exception;

public class FilmNotFoundException extends Exception{

    public FilmNotFoundException()
    {
        super("Film non trouvé");
    }
}
