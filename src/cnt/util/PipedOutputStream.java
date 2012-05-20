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
public class PipedOutputStream extends OutputStream
{
    public PipedOutputStream()
    {   this.data = new PipedDataBuffer();
	this.opposite = null;
    }
    
    public PipedOutputStream(final PipedInputStream opposite)
    {   this.data = (this.opposite = opposite).data;
    }
    
    
    
    PipedDataBuffer data;
    PipedInputStream opposite;
    
    
    
    public void write(final byte[] bs, final int off, final int len) throws IOException
    {   synchronized (this.data)
	{   for (int i = off, n = off + len; i < n; i++)
	    {   int b = (int)(bs[i]);
		this.data.write(b & 255);
	    }
	    this.data.flush();
	    if (this.opposite != null)
	    synchronized (this.opposite)
	    {   this.opposite.notifyAll();
	    } 
    }   }
    
    public void write(final byte[] bs) throws IOException
    {   this.write(bs, 0, bs.length);
    }
    
    public void write(final int b) throws IOException
    {   synchronized (this.data)
	{   this.data.write(b);
	    this.data.flush();
	    if (this.opposite != null)
		synchronized (this.opposite)
		{   this.opposite.notifyAll();
		}
    }   }
    
    public void flush() throws IOException
    {   this.data.flush();
	if (this.opposite != null)
	    synchronized (this.opposite)
	    {   this.opposite.notifyAll();
    }       }
    
}

