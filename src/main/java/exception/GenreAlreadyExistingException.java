package exception;

public class GenreAlreadyExistingException extends Exception{

    public GenreAlreadyExistingException()
    {
        super("Ce genre existe déjà");
    }
}
