import java.util.*;
import java.io.*;
import java.net.*;


public class UDPServer
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
	final int localport = Integer.parseInt(args[0]);
	final DatagramSocket socket = new DatagramSocket(localport);
	
	final byte[] buffer = new byte[512];
	
	final HashMap<InetSocketAddress, String> clients = new HashMap<InetSocketAddress, String>();
	int colour = 0;
	
	for (;;)
	{
	    final DatagramPacket p = new DatagramPacket(buffer, buffer.length);
	    socket.receive(p);
	    
	    final InetSocketAddress remote = (InetSocketAddress)(p.getSocketAddress());
	    
	    if (clients.containsKey(remote))
	    {
		System.out.print(clients.get(remote));
		System.out.write(buffer, 0, p.getLength());
		System.out.println("\033[0m");
	    }
	    else if ((p.getLength() == 2) && (buffer[0] == ENQUIRY) && (buffer[1] == END_OF_TRANSMISSION))
	    {
		clients.put(remote, "\033[0;3" + (++colour) + "m");
		
		buffer[0] = ACKNOWLEDGE;
		buffer[1] = END_OF_TRANSMISSION;
		socket.send(new DatagramPacket(buffer, 2, remote));
	    
		System.out.print("\033[0;42;33;1m");
		System.out.print("Connection established:");
		System.out.print("\033[0;3" + colour + "m");
		System.out.print(" " + remote.toString());
		System.out.println("\033[0m");
	    }
	    else
	    {
		buffer[0] = NEG_ACKNOWLEDGE;
		buffer[1] = CANCEL;
		buffer[2] = END_OF_TRANSMISSION;
		socket.send(new DatagramPacket(buffer, 3, remote));
	    
		System.out.print("\033[0;41;33;1m");
		System.out.print("Connection denied: " + remote.toString());
		System.out.println("\033[0m");
	    }
	}
    }
    
}

