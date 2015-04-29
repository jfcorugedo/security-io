package es.corugedo.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class limits the amount of bytes that this stream can read at once.
 * 
 * The status can be reset using the method resetCount:
 * <pre>
 * 	<code>
 * 		boundedInStream.read(buffer);
 * 		boundedInStream.resetCount(); //If this line is executed, the stream can read again until its current limit
 * 	</code>
 * </pre>
 * 
 * If the limit is reached, this class will throw a SizeLimitExceededException.
 * 
 * @see SizeLimitExceededException
 */
public class BoundedInputStream  extends InputStream{
	
	/** Maximum number of bytes that will read until the resetCount method is invoked. -1 means no limit */
	private long maxBytesRead = -1;
	
	private InputStream inputStream;
	
	/** the number of bytes already returned */
    private long totalBytesRead = 0;
	
    /**
     * Build an stream that will read bytes until the maximum specified.
     * 
     * @param maxBytesRead Max bytes that will be read between every resetCount calls
     * @param inputStream Stream used to read bytes
     */
	public BoundedInputStream(long maxBytesRead, InputStream inputStream) {
		this.maxBytesRead = maxBytesRead;
		this.inputStream = inputStream;
	}

	@Override
	public int read() throws IOException {
		if (maxBytesRead >= 0 && totalBytesRead >= maxBytesRead) {
            handleError();
        }
        int result = inputStream.read();
        totalBytesRead++;
        return result;
	}

	private void handleError() {
		throw new SizeLimitExceededException(String.format("Too many bytes read. The maximun limit has been reached: %d", maxBytesRead));
	}
	
	/**
	 * This method reset the internal count of this stream, so it can read the max bytes again
	 */
	public void resetCount() {
		this.totalBytesRead = 0;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (maxBytesRead>=0 && totalBytesRead>=maxBytesRead) {
			handleError();
        }
        long maxRead = maxBytesRead>=0 ? Math.min(len, maxBytesRead-totalBytesRead) : len;
        int bytesRead = this.inputStream.read(b, off, (int)maxRead);

        if (bytesRead==-1) {
            return -1;
        }

        totalBytesRead+=bytesRead;
        return bytesRead;
	}

	@Override
	public long skip(long n) throws IOException {
		return inputStream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return inputStream.available();
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		inputStream.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		inputStream.reset();
	}

	@Override
	public boolean markSupported() {
		return inputStream.markSupported();
	}
}