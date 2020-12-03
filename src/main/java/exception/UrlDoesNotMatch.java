package exception;

public class UrlDoesNotMatch extends Exception {

	public UrlDoesNotMatch (String url) {
		super("Un url youtube est attendu, celui-ci ne correspond pas : " + url);
	}
}
