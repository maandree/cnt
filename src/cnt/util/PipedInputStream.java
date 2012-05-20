/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.util;

import java.io.*;


/**
 * PipedInput/OutputStream is unsophisticated and crusty, kill it!
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class PipedInputStream extends InputStream
{
    public PipedInputStream()
    {   this.data = new PipedDataBuffer();
    }
    
    public PipedInputStream(final PipedOutputStream opposite)
    {   this.data = opposite.data;
	opposite.opposite = this;
    }
    
    
    
    PipedDataBuffer data;
    
    
    
    public int available()
    {   return this.data.available();
    }
    
    public int read() throws IOException
    {   return this.data.read();
    }
    
    public int read(final byte[] bs) throws IOException
    {   return this.read(bs, 0, bs.length);
    }
    
    public int read(final byte[] bs, final int off, final int len) throws IOException
    {
	synchronized (this.data)
	{
	    int av = this.available();
	    if (av > len)
		av = len;
	    
	    for (int i = off, n = off + len; i < n; i++)
		bs[i] = (byte)(this.data.read());
	    
	    return av;
	}
    }
    
}

