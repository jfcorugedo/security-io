package es.corugedo.io;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import static es.corugedo.io.utils.IOTestUtils.*;
import static org.assertj.core.api.Assertions.*;

public class BoundedBufferedReaderTest {

	@Test(expected=SizeLimitExceededException.class)
	public void testReadInfiniteFile() throws IOException {
		//given
		BoundedBufferedReader bf = new BoundedBufferedReader(buildInputStream(-1), 1024);
		
		//when
		bf.readLine();
		
		//The test never reaches this line
		bf.close();
	}
	
	@Test
	public void testReadSmallEnoughtStream() throws IOException {
		//given
		BoundedBufferedReader bf = new BoundedBufferedReader(buildInputStream(1023), 1024);
		
		//when
		bf.readLine();
		
		//The program always reaches this line
		bf.close();
	}
	
	@Test
	public void testRead() throws IOException {
		BoundedBufferedReader bf = new BoundedBufferedReader(buildInputStream(4), 1);
		
		for(int i = 0 ; i < 4 ; i++) {
			assertThat(bf.read()).isEqualTo('A');
		}
		
		bf.close();
	}
	
	@Test
	public void testReadBuffer() throws IOException {
		BoundedBufferedReader bf = new BoundedBufferedReader(buildInputStream(4), 2);
		
		List<Character> content = readEntireContentTakenInPairs(bf);
		
		assertThat(content).hasSize(4);
		assertThat(content).containsExactly('A', 'A', 'A', 'A');
		
		assertThat(bf.read()).isEqualTo(-1);
		
		bf.close();
	}
}
