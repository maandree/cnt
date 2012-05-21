import java.io.*;
import java.net.*;
import java.util.*;


public class NetInfo
{
    public static void main(final String... args) throws Exception
    {
	final Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
	while (networks.hasMoreElements())
	    try
	    {
		final NetworkInterface network = networks.nextElement();
		
		
		System.out.println(network.getName());
		
		System.out.println("    Display name: " + network.getDisplayName());
		System.out.println("    MTU: " + network.getMTU());
		System.out.println("    Parent: " + (network.getParent() == null ? " \033[35m(none)\033[39m" : network.getParent().getName()));
		System.out.println("    Index: " + network.getIndex());
		System.out.println("    Is loopback: " + network.isLoopback());
		System.out.println("    Is point to point: " + network.isPointToPoint());
		System.out.println("    Is up: " + network.isUp());
		System.out.println("    Is virtual: " + network.isVirtual());
		System.out.println("    Supports multicast: " + network.supportsMulticast());
		
		try
		{
		    final byte[] mac = network.getHardwareAddress();
		    System.out.print("    MAC: " + Integer.toHexString((mac[0] >>> 4) & 15) + Integer.toHexString(mac[0] & 15));
		    for (int i = 1, n = mac.length; i < n; i++)
			System.out.print(":" + Integer.toHexString((mac[i] >>> 4) & 15) + Integer.toHexString(mac[i] & 15));
		    System.out.println();
		}
		catch (final Throwable err)
		{
		    System.out.println("    MAC: \033[31m(none)\033[39m");
		}
		
		System.out.println("    Interface addresses:");
		for (final InterfaceAddress address : network.getInterfaceAddresses())
		    System.out.println("        " + address.toString());
		
		
		System.out.println("    Internet addresses:");
		final Enumeration<InetAddress> addresses = network.getInetAddresses();
		while (addresses.hasMoreElements())
		{
		    final InetAddress address = addresses.nextElement();
		    System.out.println("        " + address.toString());
		    System.out.println("            Canonical: " + address.getCanonicalHostName());
		    System.out.println("            Address: " + address.getHostAddress());
		    System.out.println("            Name: " + address.getHostName());
		}
		
		for (;;)
		    if (System.in.read() == 10)
			break;
	    }
	    catch (final Throwable err)
	    {
		System.out.println("\033[31merror\033[39m");
	    }
	
	
	final String publicip;
	
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
	    System.out.println("Public IP: " + (publicip = line));
	}
	final InetAddress[] addresses = InetAddress.getAllByName(publicip);
	for (final InetAddress address : addresses)
	{
	    System.out.println("    " + address.toString());
	    System.out.println("        Canonical: " + address.getCanonicalHostName());
	    System.out.println("        Address: " + address.getHostAddress());
	    System.out.println("        Name: " + address.getHostName());
	}
	
    }
    
}

