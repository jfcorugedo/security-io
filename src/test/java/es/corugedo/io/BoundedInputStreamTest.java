package es.corugedo.io;

import static es.corugedo.io.utils.IOTestUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.Test;

public class BoundedInputStreamTest {

	@Test(expected=SizeLimitExceededException.class)
	public void testReachMaximumBytes() throws IOException {
		//given
		BoundedInputStream boundedStream = new BoundedInputStream(5, buildInputStream(6));
		
		//when
		readEntireContentTakenInPairs(boundedStream);
		
		//The program never reaches this line
		boundedStream.close();
	}
	
	@Test
	public void testReadAllBytes() throws IOException {
		//given
		BoundedInputStream boundedStream = new BoundedInputStream(5, buildInputStream(4));
		
		//when
		readEntireContentTakenInPairs(boundedStream);
		
		//The program always reaches this line
		boundedStream.close();
	}
	
	@Test
	public void testReadAllBytesOneByOne() throws IOException {
		//given
		BoundedInputStream boundedStream = new BoundedInputStream(5, buildInputStream(4));
		
		//when
		readEntireContentTakenOneByOne(boundedStream);
		
		//The program always reaches this line
		boundedStream.close();
	}
	
	@Test(expected=SizeLimitExceededException.class)
	public void testReachMaximumBytesReadingOneByOne() throws IOException {
		//given
		BoundedInputStream boundedStream = new BoundedInputStream(5, buildInputStream(6));
		
		//when
		readEntireContentTakenOneByOne(boundedStream);
		
		//The program never reaches this line
		boundedStream.close();
	}
}
