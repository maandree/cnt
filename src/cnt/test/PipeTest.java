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
	/*{
	    final java.io.PipedInputStream pis = new java.io.PipedInputStream()
		    {
			{
			    (new Thread() {
				    public void run()
				    {
					try
					    {
					for (;;)
					    synchronized (this)
					    {
						this.wait();
						System.out.println("notified");
					    }
					    }
					catch (final Throwable err)
					    {
					    }
				    }
				}).start();
			}
			
			public int read() throws IOException
			{
			    System.out.println("<read()>");
			    return super.read();
			}
			public int read(final byte[] b) throws IOException
			{
			    System.out.println("<read([byte)>");
			    return super.read(b);
			}
			public int read(final byte[] b, final int off, final int len) throws IOException
			{
			    System.out.println("<read([byte,int,int)>");
			    return super.read(b, off, len);
			}
			public int available() throws IOException
			{
			    System.out.println("<available()>");
			    return super.available();
			}
			public long skip(final long n) throws IOException
			{
			    System.out.println("<skip(long)>");
			    return super.skip(n);
			}
			public void close() throws IOException
			{
			    System.out.println("<close()>");
			    super.close();
			}
			public void reset() throws IOException
			{
			    System.out.println("<reset()>");
			    super.reset();
			}
			public void mark(final int readlimit)
			{
			    System.out.println("<mark(int)>");
			    super.mark(readlimit);
			}
			public boolean markSupported()
			{
			    System.out.println("<markSupported()>");
			    return super.markSupported();
			}
		    };
	    final java.io.PipedOutputStream pos = new java.io.PipedOutputStream(pis);
	    
	    final InputStream bis = new BufferedInputStream(pis);
	    final OutputStream bos = new BufferedOutputStream(pos);
	    
	    for (int i = 0; i < 10; i++)
		bos.write(i % 10);
	    
	    bos.flush();
	    
	    for (int i = 0; i < 10; i++)
		System.out.print(bis.read());
	
	    System.out.println();
	}/**/
	/*{
	    final PipedInputStream pis = new PipedInputStream();
	    final PipedOutputStream pos = new PipedOutputStream(pis);
	
	    for (int i = 0; i < 200; i++)
		pos.write(i % 10);
	
	    for (int i = 0; i < 200; i++)
		System.out.print(pis.read());
	
	    System.out.println();
	}/**/
	/**/{
	    final PipedInputStream pis = new PipedInputStream();
	    final PipedOutputStream pos = new PipedOutputStream(pis);
	    
	    //final InputStream bis = pis;
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
	}/**/
    }
}
