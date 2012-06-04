/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;
import cnt.local.*;
import cnt.game.*;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Network diagnostics kit
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Diagnostics
{
    /**
     * Non-constructor
     */
    private Diagnostics()
    {
        assert false : "You may not create instances of this class [Diagnostics].";
    }
    
    
    
    /**
     * Runs a network diagnostics
     * 
     * @return  A tree structured string, where each identent is one space
     */
    public static String run()
    {
	return run(0, 5_000);
    }
    
    /**
     * Runs a network diagnostics
     * 
     * @param   ttl      Connection time to live, 0 for default
     * @param   timeout  Connection timeout is milliseconds
     * @return           A tree structured string, where each identent is one space
     */
    public static String run(final int ttl, final int timeout)
    {
	final StringBuilder buf = new StringBuilder();
	
	buf.append("Network interfaces\n");
	try
	{
	    final Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
	    while (networks.hasMoreElements())
		try
		{
		    final NetworkInterface network = networks.nextElement();
		    
		    
		    buf.append(" " + network.getName() + "\n");
		    
		    buf.append("  Display name: " + network.getDisplayName() + "\n");
		    buf.append("  MTU: " + network.getMTU() + "\n");
		    buf.append("  Parent: " + (network.getParent() == null ? "(none)" : network.getParent().getName()) + "\n");
		    buf.append("  Index: " + network.getIndex() + "\n");
		    buf.append("  Is loopback: " + network.isLoopback() + "\n");
		    buf.append("  Is point to point: " + network.isPointToPoint() + "\n");
		    buf.append("  Is up: " + network.isUp() + "\n");
		    buf.append("  Is virtual: " + network.isVirtual() + "\n");
		    buf.append("  Supports multicast: " + network.supportsMulticast() + "\n");
		    
		    try
		    {
			final byte[] mac = network.getHardwareAddress();
			buf.append("  MAC: " + Integer.toHexString((mac[0] >>> 4) & 15) + Integer.toHexString(mac[0] & 15));
			for (int i = 1, n = mac.length; i < n; i++)
			    buf.append(":" + Integer.toHexString((mac[i] >>> 4) & 15) + Integer.toHexString(mac[i] & 15));
			buf.append("\n");
		    }
		    catch (final Throwable err)
		    {
			buf.append("  MAC: (none)\n");
		    }
		    
		    buf.append("  Interface addresses\n");
		    for (final InterfaceAddress address : network.getInterfaceAddresses())
			buf.append("   " + address.toString() + "\n");
		    
		    buf.append("  Internet addresses\n");
		    final Enumeration<InetAddress> addresses = network.getInetAddresses();
		    while (addresses.hasMoreElements())
		    {
			final InetAddress address = addresses.nextElement();
			buf.append("   " + address.toString() + "\n");
			buf.append("    Canonical: " + address.getCanonicalHostName() + "\n");
			buf.append("    Address: " + address.getHostAddress() + "\n");
			buf.append("    Name: " + address.getHostName() + "\n");
		    }
		}
		catch (final Throwable err)
		{
		    buf.append("  error\n");
		}
	}
	catch (final Throwable err)
	{
	    buf.append(" error\n");
	}
	
	buf.append("Local client\n");
	String publicip = null;
	try
	{
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
		buf.append(" Public IP: " + (publicip = line) + "\n");
	    }
	    final InetAddress[] addresses = InetAddress.getAllByName(publicip);
	    for (final InetAddress address : addresses)
	    {
		buf.append("  " + address.toString() + "\n");
		buf.append("   Canonical: " + address.getCanonicalHostName() + "\n");
		buf.append("   Address: " + address.getHostAddress() + "\n");
		buf.append("   Name: " + address.getHostName() + "\n");
	    }
	}
	catch (final Throwable err)
	{
	    buf.append(" Unable to get public IP address.\n");
	}
	
	for (final String dns : Friends.getPersonalDNSes())
        {
	    buf.append(" " + dns + "\n");
	    try
	    {
		final InetAddress[] addresses = InetAddress.getAllByName(dns);
		for (final InetAddress address : addresses)
		{
		    buf.append("  " + address.toString() + "\n");
		    buf.append("   Canonical: " + address.getCanonicalHostName() + "\n");
		    buf.append("   Address: " + address.getHostAddress() + "\n");
		    buf.append("   Name: " + address.getHostName() + "\n");
		}
	    }
	    catch (final Throwable err)
	    {
		buf.append("  error\n");
	    }
	}
	
	buf.append("Friends\n");
	final Player[] friends = Friends.getFriends();
	for (final Player friend : friends)
	{
	    buf.append(" " + friend.getName() + "\n");
	    buf.append("  UUID: " + friend.getUUID() + "\n");
	    
	    final String[] dnses = new String[friend.getDNSes().size()];
	    friend.getDNSes().toArray(dnses);
	    final String xip, lip;
	    for (final String[] arr : new String[][] {{xip = friend.getPublicIP(), lip = friend.getLocalIP()}, dnses})
		for (final String dns : arr)
		{
		    buf.append("  " + dns);
		    if (dns == lip)
			if (publicip == null)
			{
			    buf.append(" (Do not known if on same network)\n");
			    continue;
			}
			else if (xip.equals(publicip))
			    buf.append(" (Local network)\n");
			else
			{
			    buf.append(" (Not on same network)\n");
			    continue;
			}
		    else
			buf.append("\n");
		    try
		    {
			final InetAddress[] addresses = InetAddress.getAllByName(dns);
			for (final InetAddress address : addresses)
			{
			    buf.append("   " + address.toString() + "\n");
			    buf.append("    Canonical: " + address.getCanonicalHostName() + "\n");
			    buf.append("    Address: " + address.getHostAddress() + "\n");
			    buf.append("    Name: " + address.getHostName() + "\n");
			    buf.append("    Reachability\n");
			    
			    final Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			    while (networks.hasMoreElements())
			    {
				final NetworkInterface network = networks.nextElement();
				String line = "     " + network.getName() + ": ";
				
				if (network.isLoopback())
				    line += "loopback";
				else if (network.isUp() == false)
				    line += "down";
				else
				    try
				    {
					if (address.isReachable(network, ttl, timeout))
					    line += "REACHABLE";
					else
					    line += "unreachable(ttl: " + (ttl == 0 ? "default" : ttl) + ", timeout [ns]: " + timeout + ")";
				    }
				    catch (final Throwable err)
				    {
					line += "error";
				    }
				
				buf.append(line + "\n");
			    }
			}
		    }
		    catch (final Throwable err)
		    {
			buf.append("   error\n");
		    }
		}
	}
	
	
	return buf.toString();
    }
    
}

