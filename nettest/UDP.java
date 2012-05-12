import java.io.*;
import java.net.*;


public class UDP
{
    private static final byte CONNECT = 1;
    private static final byte ACK = 2;
    
    public static void main(final String... args) throws Throwable
    {
	final int localport = Integer.parseInt(args[0]);
	final String remotehost = args[1];
	final int remoteport = Integer.parseInt(args[2]);
	
	final DatagramSocket socket = new DatagramSocket(localport);
	
	final byte buffer[] = new byte[32];
	
	buffer[0] = CONNECT;
	socket.send(new DatagramPacket(buffer, 1, InetAddress.getAllByName(remotehost)[0], remoteport));
	System.out.println("Sent CONNECT bytes to [" + remotehost + "]:" + remoteport + " (may be lost)"); 
	
	final DatagramPacket p = new DatagramPacket(buffer, buffer.length);
	socket.receive(p);
	
	System.out.println("Received " + p.getLength() + " bytes from " + p.getSocketAddress().toString());
	
	if (p.getLength() == 1)
	    if (buffer[0] == CONNECT)
	    {
		System.out.println("Connected");
		
		buffer[0] = ACK;
		socket.send(new DatagramPacket(buffer, 1, InetAddress.getAllByName(remotehost)[0], remoteport));
		System.out.println("Sent ACK bytes to [" + remotehost + "]:" + remoteport); 
	    }
	    else if (buffer[0] == ACK)
	    {
		System.out.println("Connected");
	    }
    }
}

