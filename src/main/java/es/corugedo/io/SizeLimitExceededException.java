package es.corugedo.io;

/**
 * This exception will be thrown whenever a stream tries to read more bytes than it's allowed.
 * 
 * This error prevents DoS attacks as well as out of memory errors
 * 
 * @author jfcorugedo
 *
 */
public class SizeLimitExceededException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SizeLimitExceededException(String message) {
		super(message);
	}

}
