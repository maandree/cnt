package cnt.util;

import java.io.IOException;
import java.io.OutputStream;


public class PipedOutputStream extends OutputStream
{
    public PipedOutputStream(final PipedInputStream sink)  throws IOException
    {
	connect(sink);
    }
    
    public PipedOutputStream()
    {
    }
    
    
    
    private PipedInputStream sink;
    
    
    
    public void connect(final PipedInputStream sink)
    {
        this.sink = sink;
        sink.in = -1;
        sink.out = 0;
    }
    
    public void write(final int b)  throws IOException
    {
        this.sink.receive(b);
    }
    
    public void write(final byte b[], final int off, final int len) throws IOException
    {
        this.sink.receive(b, off, len);
    }
    
    public synchronized void flush() throws IOException
    {
        if (this.sink != null)
            synchronized (this.sink)
	    {
                this.sink.notifyAll();
            }
    }
    
}
