package exception;

public class GenreLinkToFilmException extends Exception{
	private static final long serialVersionUID = 1L;

	public GenreLinkToFilmException()
    {
        super("Ce genre est lié à un ou plusieurs film");
    }
}
