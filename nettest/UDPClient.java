import java.util.*;
import java.io.*;
import java.net.*;


public class UDPClient
{
    //control characters
    private static final byte NULL                      = 0x00;
    private static final byte START_OF_HEADING          = 0x01;
    private static final byte START_OF_TEXT             = 0x02;
    private static final byte END_OF_TEXT               = 0x03;
    private static final byte END_OF_TRANSMISSION       = 0x04;
    private static final byte ENQUIRY                   = 0x05;
    private static final byte ACKNOWLEDGE               = 0x06;
    private static final byte NEG_ACKNOWLEDGE           = 0x15;
    private static final byte END_OF_TRANSMISSION_BLOCK = 0x17;
    private static final byte CANCEL                    = 0x18;
    private static final byte STRING_TERMINATOR         = (byte)0x9C;
    
    
    
    
    public static void main(final String... args) throws Throwable
    {
	final String remotehost = args[0];
	final int remoteport = Integer.parseInt(args[1]);
	final DatagramSocket socket = new DatagramSocket();
	
	final byte buffer[] = new byte[512];
	
	final InetSocketAddress remote = new InetSocketAddress(remotehost, remoteport);
	
	buffer[0] = ENQUIRY;
	buffer[1] = END_OF_TRANSMISSION;
	socket.send(new DatagramPacket(buffer, 2, remote));
	
	final DatagramPacket p = new DatagramPacket(buffer, buffer.length);
	socket.receive(p);
	
	if ((p.getLength() == 2) && (buffer[0] == ACKNOWLEDGE) && (buffer[1] == END_OF_TRANSMISSION))
	    System.out.println("Connection established");
	else
	{
	    System.out.println("Connection failed");
	    return;
	}
	
	for (;;)
	{
	    int ptr = 0;
	    for (int d; (d = System.in.read()) != '\n';)
		buffer[ptr++] = (byte)d;
	    
	    socket.send(new DatagramPacket(buffer, ptr, remote));
	}
    }
    
}

