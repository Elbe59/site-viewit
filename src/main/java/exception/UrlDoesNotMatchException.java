package exception;

public class UrlDoesNotMatchException extends Exception {
	private static final long serialVersionUID = 1L;

	public UrlDoesNotMatchException (String url) {
		super("Un url youtube est attendu, celui-ci ne correspond pas : " + url);
	}
}
