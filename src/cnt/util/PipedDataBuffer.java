/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.util;


/**
 * PipedInput/OutputStream is unsophisticated and crusty, kill it!
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
class PipedDataBuffer
{
    //Has default constructor
    
    
    
    private int[] data = new int[128];
    private int rptr = 0;
    private int wptr = 0;
    
    
    
    public synchronized int available()
    {
	return this.wptr - this.rptr;
    }
    
    public synchronized int read()
    {
	if (this.rptr == this.wptr)
	    try
	    {   this.wait();
	    }
	    catch (final InterruptedException err)
	    {   //Ignore
	    }
	
	return this.data[this.rptr++];
    }
    
    public synchronized void write(int b)
    {
	if (this.wptr == this.data.length)
	    if (this.rptr == 0)
	    {
		final int[] ndata = new int[this.wptr << 1];
		System.arraycopy(this.data, 0, ndata, 0, this.wptr);
		this.data = ndata;
	    }
	    else
	    {
		final int[] ndata = new int[this.wptr];
		System.arraycopy(this.data, this.rptr, ndata, 0, this.wptr -= this.rptr);
		this.data = ndata;
		this.rptr = 0;
	    }
	
	this.data[this.wptr++] = b;
    }
    
    public synchronized void flush()
    {
	this.notifyAll();
    }
    
}

