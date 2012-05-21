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
    
}

