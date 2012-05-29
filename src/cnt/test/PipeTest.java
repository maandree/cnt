/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.test;
import cnt.util.PipedInputStream;
import cnt.util.PipedOutputStream;

import java.io.*;


/**
 * Piped I/O test
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class PipeTest
{
    /**
     * Non-constructor
     */
    private PipeTest()
    {
	assert false : "You may not create instances of this class [PipeTest].";
    }
    
    
    
    /**
     * This is the main entry point of the test
     * 
     * @param  args  Startup arguments, unused
     * 
     * @throws  IOException  Will not be thrown
     */
    public static void main(String... args) throws IOException
    {
	final PipedInputStream pis = new PipedInputStream();
	final PipedOutputStream pos = new PipedOutputStream(pis);
	
	final InputStream bis = new BufferedInputStream(pis);
	final OutputStream bos = new BufferedOutputStream(pos);
	
	for (int i = 0; i < 10; i++)
	    bos.write(i % 10);
	
	bos.flush();
	
	byte[] bs = new byte[10];
	bis.read(bs, 0, bs.length);
	for (int i = 0; i < bs.length; i++)
	    System.out.print(bs[i]);
	
	System.out.println();
    }
}
