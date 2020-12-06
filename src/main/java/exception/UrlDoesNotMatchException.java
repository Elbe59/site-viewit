package exception;

public class UrlDoesNotMatchException extends Exception {

	public UrlDoesNotMatchException (String url) {
		super("Un url youtube est attendu, celui-ci ne correspond pas : " + url);
	}
}
