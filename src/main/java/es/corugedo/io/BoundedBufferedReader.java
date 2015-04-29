package es.corugedo.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

/**
 * This class can read lines of a file, just in the same way as BufferedReader, but avoiding
 * security problems.
 * 
 * While BufferedReader.readLine reads all the characters of the file until it finds a line feed
 * character, BoundedBufferdReader reads until it finds a line feed character or it reaches the 
 * maximum amount of bytes specified.
 * 
 * Each time it found a line end character, it resets the bytes read so the next line can
 * read the maximum amount of bytes allowed.
 * 
 * This class prevents DoS attacks as well as out of memory errors
 *     
 * @author jfcorugedo
 *
 */
public class BoundedBufferedReader extends Reader {
	
	/** Default encoding that will be used if no encoding is specified in the constructor */
	public static final String DEFAULT_CHARSET = "ISO-8859-1";

	private BufferedReader simpleBufferedReader;
	
	private BoundedInputStream boundedInStream;
	
	/**
	 * Creates a new BoundedBufferedReader that will read form the InputStream characters with
	 * the specified encoding.
	 * 
	 * This instance never reads a line with more bytes than those specified in this constructor.
	 * 
	 * @param inputStream Stream used to read characters
	 * @param maxLineLengthInBytes Maximum length of a line. This object never stores more than these characters in its internal buffer
	 * @param encoding Encoding used to convert bytes into characters
	 * @throws UnsupportedEncodingException
	 */
	public BoundedBufferedReader(InputStream inputStream, long maxLineLengthInBytes, String encoding) throws UnsupportedEncodingException {
		this.boundedInStream = new BoundedInputStream(maxLineLengthInBytes, inputStream);
		this.simpleBufferedReader = new BufferedReader(new InputStreamReader(boundedInStream, encoding));
	}
	
	/**
	 * Creates a new BoundedBufferedReader that will read form the InputStream characters with
	 * the default encoding: {@link BoundedBufferedReader#DEFAULT_CHARSET}.
	 * 
	 * This instance never reads a line with more characters than those specified in this contructor.
	 * 
	 * @param inputStream Stream used to read characters
	 * @param maxLineLength Maximum length of a line. This object never stores more than these characters in its internal buffer
	 * @throws UnsupportedEncodingException
	 */
	public BoundedBufferedReader(InputStream inputStream, long maxLineLength) throws UnsupportedEncodingException {
		this(inputStream, maxLineLength, DEFAULT_CHARSET);
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int result = this.simpleBufferedReader.read();
		this.boundedInStream.resetCount();
		return result;
	}

	@Override
	public void close() throws IOException {
		this.simpleBufferedReader.close();
	}

	@Override
	public int read(CharBuffer target) throws IOException {
		int result = this.simpleBufferedReader.read(target);
		this.boundedInStream.resetCount();
		return result;
	}

	@Override
	public int read() throws IOException {
		int result = this.simpleBufferedReader.read();
		this.boundedInStream.resetCount();
		return result;
	}

	@Override
	public int read(char[] cbuf) throws IOException {
		int result = this.simpleBufferedReader.read(cbuf);
		this.boundedInStream.resetCount();		
		return result;
	}

	@Override
	public long skip(long n) throws IOException {
		
		return this.simpleBufferedReader.skip(n);
	}

	@Override
	public boolean ready() throws IOException {
		
		return this.simpleBufferedReader.ready();
	}

	@Override
	public boolean markSupported() {
		
		return this.simpleBufferedReader.markSupported();
	}

	@Override
	public void mark(int readAheadLimit) throws IOException {
		
		this.simpleBufferedReader.mark(readAheadLimit);
	}

	@Override
	public void reset() throws IOException {
		
		this.simpleBufferedReader.reset();
	}

	/**
     * Reads a line of text.  A line is considered to be terminated by any one
     * of a line feed ('\n'), a carriage return ('\r'), or a carriage return
     * followed immediately by a line feed.
     * 
     * This method reads until it finds one of these characters or it reaches the
     * maximum characters specified in the constructor of this object.
     * 
     * If the limit is reached, this class will throw a SizeLimitExceededException.
     *
     * @return     A String containing the contents of the line, not including
     *             any line-termination characters, or null if the end of the
     *             stream has been reached
     *
     * @exception  IOException  If an I/O error occurs
     * @exception SizeLimitExceededException If the maximum characters has been read but no line feed has been found
     *
     * @see java.nio.file.Files#readAllLines
     */
	public String readLine() throws IOException, SizeLimitExceededException {
		String line = this.simpleBufferedReader.readLine();
		this.boundedInStream.resetCount();
		return line;
	}
}
