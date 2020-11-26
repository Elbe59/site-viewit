package exception;

public class GenreLinkToFilmException extends Exception{

    public GenreLinkToFilmException()
    {
        super("Ce genre est lié à un ou plusieurs film");
    }
}
