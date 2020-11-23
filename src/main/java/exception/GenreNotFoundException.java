package exception;

public class GenreNotFoundException extends Exception{

    public GenreNotFoundException()
    {
        super("Genre non trouvé");
    }
}
