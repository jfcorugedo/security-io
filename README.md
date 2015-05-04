# security-io

The main purpose of this project is create some classes that can be used when there is some probabilities of DoS attacks as well as to avoid out of memory problems while reading files of an untrusted source.

#Usage

Use one of the two classes provided:

## BoundedBufferedReader

This class can read lines of a file, just in the same way as BufferedReader, but avoiding security problems.

While `BufferedReader.readLine` reads all the characters of the file until it finds a line feed character, `BoundedBufferdReader.readLine` reads until it finds a line feed character or it reaches the  maximum amount of bytes specified.

Each time it found a line end character, it resets the bytes read so the next line can read the maximum amount of bytes allowed.

So it can read lines in the same way as traditional `BufferedReader` while the target file doesn't have lines longer than the maximum bytes specified in the constructor.

This class prevents DoS attacks as well as out of memory errors.

## BoundedInputStream

This class limits the amount of bytes that this stream can read at once.
 
The status can be reset using the method resetCount:

      boundedInStream.read(buffer);
      boundedInStream.resetCount(); //If this line is executed, the stream can read again until its current limit
 
If the limit is reached, this class will throw a `SizeLimitExceededException`.
