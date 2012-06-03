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
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Toolkit
{
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

    public static String getLocalIP()
    {
	/*
	* This is all because InetAddress.getLocalHost.getHostAddress() returns 127.0.1.1 to where we cannot portforward
	* See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037 for details of problem.
	* This hopefully solves it. 
	*/
	try {
		//Get all interfaces
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		{
			NetworkInterface iface = en.nextElement();
			// If the interface is not up, then we don't want to use it.
			if (!iface.isUp())
				continue;
			
			// Get ALL address listed on the interface
			for (InterfaceAddress eth : iface.getInterfaceAddresses())
			{
				
				System.out.println("\n\nPossible Address: [" + eth.getAddress().toString().substring(1) + "]\n");
				// We don't want loopback or IPv6. TODO: better way of sorting out IPv6
				if ( !eth.getAddress().isLoopbackAddress() && !eth.getAddress().toString().contains(":") )
				{
					// Choose the first address that is plausable, hopefully it is the correct one. Might cause problems on multihomed machines.
					System.out.println("\nChoosen Address: [" + eth.getAddress().toString().substring(1) + "]\n");
					// Remove \ from begining of string and return it
					return eth.getAddress().toString().substring(1);
				}
			}
		}
	} catch (SocketException se) {
		System.err.println("\n\nError with socket determening internal IP\n");
	}
	// If no IP was found return empty string
	return "";
    }
}

