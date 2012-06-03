package cnt.util;

import java.io.IOException;
import java.io.InputStream;


public class PipedInputStream extends InputStream
{
    public PipedInputStream(final PipedOutputStream src) throws IOException
    {
	src.connect(this);
    }
    
    public PipedInputStream()
    {
    }
    
    
    
    protected byte buffer[] = new byte[1024];
    
    protected int in = -1;
    protected int out = 0;
    
    
    
    protected synchronized void receive(final int b) throws IOException
    {
        if (this.in == this.out)
            awaitSpace();
	
        if (this.in < 0)
            this.in = this.out = 0;
	
        this.buffer[this.in++] = (byte)(b & 0xFF);
	
        if (this.in >= this.buffer.length)
            this.in = 0;
    }
    
    synchronized void receive(final byte b[], final int off, final int len) throws IOException
    {
	int coff = off;
        int bytesToTransfer = len;
        while (bytesToTransfer > 0)
	{
            if (this.in == this.out)
                awaitSpace();
            int nextTransferAmount = 0;
            if (this.out < this.in)
                nextTransferAmount = this.buffer.length - this.in;
            else if (this.in < this.out)
                if (this.in == -1)
		{
                    this.in = this.out = 0;
                    nextTransferAmount = this.buffer.length - this.in;
                }
		else
                    nextTransferAmount = this.out - this.in;
            if (nextTransferAmount > bytesToTransfer)
                nextTransferAmount = bytesToTransfer;
	    
            System.arraycopy(b, coff, this.buffer, this.in, nextTransferAmount);
	    
            bytesToTransfer -= nextTransferAmount;
            coff += nextTransferAmount;
            this.in += nextTransferAmount;
	    
            if (this.in >= this.buffer.length)
                this.in = 0;
        }
    }
    
    
    private void awaitSpace() throws IOException
    {
        while (this.in == this.out)
        {   notifyAll();
            try
	    {   wait(1000);
            } catch (InterruptedException ex)
	    {   throw new java.io.InterruptedIOException();
        }   }
    }
    
    
    public synchronized int read()  throws IOException
    {
        while (this.in < 0)
	{   notifyAll();
            try
	    {   wait(1000);
            }
	    catch (InterruptedException ex)
	    {   throw new java.io.InterruptedIOException();
        }   }
	
        int ret = this.buffer[this.out++] & 0xFF;
	
        if (this.out >= this.buffer.length)
            this.out = 0;
        if (this.in == this.out)
            this.in = -1;

        return ret;
    }
    
    public synchronized int read(final byte b[], final int off, final int len) throws IOException
    {
        if (len == 0)
            return 0;
	
        int c = read();
        if (c < 0)
            return -1;
	
        b[off] = (byte)c;
        int rlen = 1;
	int clen = len;
        while ((this.in >= 0) && (clen > 1))
	{
            int available;
	    
            if (this.in > this.out)  available = Math.min(this.buffer.length - this.out, this.in - this.out);
	    else                     available = this.buffer.length - this.out;
	    
            if (available > clen - 1)
                available = clen - 1;
            
            System.arraycopy(this.buffer, this.out, b, off + rlen, available);
            this.out += available;
            rlen += available;
            clen -= available;
	    
            if (this.out >= this.buffer.length)
                this.out = 0;
	    
            if (this.in == this.out)
                this.in = -1;
        }
        return rlen;
    }
    
    public synchronized int available() throws IOException
    {
        if (this.in < 0)          return 0;
        if (this.in == this.out)  return this.buffer.length;
        if (this.in > this.out)   return this.in - this.out;
	
	return this.in + this.buffer.length - this.out;
    }
}
