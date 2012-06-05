/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Network tool kit
 * 
 * @author Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Toolkit
{
    /**
     * Non-constructor
     */
    private Toolkit()
    {
        assert false : "You may not create instances of this class [Toolkit].";
    }
    
    
    
    /**
     * Gets the LAN's public IP address
     * 
     * @return  The LAN's public IP address
     * 
     * @throws  IOException  If it was not possible to get the IP address
     */
    public static String getPublicIP() throws IOException
    {
	final Socket sock = new Socket("checkip.dyndns.org", 80);
	final InputStream is = new BufferedInputStream(sock.getInputStream());
	final OutputStream os = new BufferedOutputStream(sock.getOutputStream());
	    
	final Scanner in = new Scanner(is);
	final PrintStream out = new PrintStream(os);
	    
	out.print("GET / HTTP/1.1\r\n");
	out.print("Host: checkip.dyndns.org\r\n");
	out.print("\r\n");
	out.flush();
	    
	for (;;)
	    if (in.nextLine().isEmpty())
		break;
	    
	String line = in.nextLine();
	sock.close();
	    
	line = line.substring(0, line.indexOf("</body>"));
	line = line.substring(line.lastIndexOf(' ') + 1);
	
	return line;
    }
    
    
    /**
     * Gets the LAN local IP address of the machine
     * 
     * @return  The LAN local IP address
     */
    public static String getLocalIP()
    {
	// This is all because InetAddress.getLocalHost().getHostAddress() returns loopback (127.0.*.1) to where we cannot portforward
	// See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037 for details of problem.
	// This hopefully solves it. 
	
	try
        {
	    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) //Get all interfaces
	    {
		NetworkInterface iface = en.nextElement();
		if (!iface.isUp()) // If the interface is not up, then we don't want to use it.
		    continue;
		
		for (InterfaceAddress eth : iface.getInterfaceAddresses()) // Get ALL addresses listed on the interface
		{
		    System.err.println("Possible Address: " + eth.getAddress().getHostAddress());
		    
		    // We don't want loopback or IPv6. TODO: better way of sorting out IPv6
		    if ((eth.getAddress().isLoopbackAddress() == false) && (eth.getAddress().getHostAddress().contains(":") == false))
		    {
			System.err.println("Choosen Address: " + eth.getAddress().getHostAddress());
			return eth.getAddress().getHostAddress();
		    }
		}
	    }
	}
	catch (SocketException se)
	{    System.err.println("Error with socket determening internal IP");
	}
	
	// If no IP was found return empty string
	return "";
    }
    
    
    /**
     * Tests whether a host is reachable
     * 
     * @param   host  The remote host's address, IP or DNS
     * @return        Whether the host is reachable
     */
    public static boolean isReachable(final String host)
    {
	try
	{   return InetAddress.getByName(host).isReachable(5_000);
	}
	catch (final Exception err)
        {   return false;
	}
    }
    
    
    
    /**
     * Gets a random port
     * 
     * @return  A random port
     * 
     * @throws  IOException  If a port cannot be choosen
     */
    public static int getRandomPort() throws IOException
    {
	final ServerSocket socket = new ServerSocket(0);
	final int port = socket.getLocalPort();
	socket.close();
	return port;
    }
    
    
}

