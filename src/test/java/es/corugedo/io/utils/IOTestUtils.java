package es.corugedo.io.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used in some tests
 * 
 * @author jfcorugedo
 *
 */
public final class IOTestUtils {

	private IOTestUtils() {
		
	}
	
	/**
	 * Builds an input stream that will returns the number of bytes specified
	 * 
	 * @param maxBytes
	 * @return
	 */
	public static final InputStream buildInputStream(final long maxBytes) {
		
		return new InputStream() {
			
			private long currentCharactersReturned = 0L;
			
			@Override
			public int read() throws IOException {
				if(maxBytes != -1 && currentCharactersReturned >= maxBytes) {
					return -1;
				}
				currentCharactersReturned++;
				return 'A';
			}
		};
	}
	
	public static final List<Character> readEntireContentTakenInPairs(Reader bf) throws IOException {
		
		List<Character> content = new ArrayList<Character>();
		char[] buffer = new char[2];
		boolean stop = false;
		while(!stop) {
			stop = bf.read(buffer) == -1;
			if(!stop) {
				for(char character : buffer) {
					content.add(character);
				}
			}
		}
		return content;
	}
	
	public static final List<Byte> readEntireContentTakenInPairs(InputStream stream) throws IOException {
		
		List<Byte> content = new ArrayList<Byte>();
		byte[] buffer = new byte[2];
		boolean stop = false;
		while(!stop) {
			stop = stream.read(buffer) == -1;
			if(!stop) {
				for(byte byteRead : buffer) {
					content.add(byteRead);
				}
			}
		}
		return content;
	}
	
	public static final List<Byte> readEntireContentTakenOneByOne(InputStream stream) throws IOException {
		
		List<Byte> content = new ArrayList<Byte>();
		byte byteRead = 0;
		while(byteRead != -1) {
			byteRead = (byte)stream.read();
			if(byteRead != -1) {
				content.add(byteRead);
			}
		}
		return content;
	}
}
