/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.local;
import cnt.game.*;

import java.util.*;
import java.io.*;


/**
 * Handles the players firend list 
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Friends
{
    /**
     * Non-constructor
     */
    private Friends()
    {
	assert false : "You may not create instances of this class [Friends].";
    }
    
    
    
    /**
     * Class initialiser
     */
    static
    {
	monitor = new Object();
	load();
    }
    
    
    
    /**
     * Synchronisation monitor
     */
    private static final Object monitor;
    
    /**
     * The local user's UUIḌ
     */
    private static UUID myUUID;
    
    /**
     * The local user's DNS names
     */
    private static ArrayList<String> myDNSes;
    
    /**
     * The file with the local user data
     */
    private static String myFile;
    
    /**
     * Set with all friends
     */
    private static HashSet<Player> friends;
    
    /**
     * The file with the friend data
     */
    private static String friendFile;
    
    
    
    /**
     * Loads everything
     */
    private static void load()
    {
	synchronized (monitor)
	{
	    myDNSes = new ArrayList<String>();
	
	    String dir = System.getProperty("user.home");
	    final String dirsep = System.getProperty("file.separator");
	    
	    if (dir.endsWith(dirsep) == false)
		dir += dirsep + ".cnt" + dirsep;
	    else
		dir += ".cnt" + dirsep;
	
	    try
	    {
		final File file = new File(dir);
		if (file.exists() == false)
		    file.mkdir();
	    
		loadFriends(dir + "friends");
		loadMe(dir + "local");
	    }
	    catch (final IOException err)
	    {
		throw new IOError(err);
	    }
	}
    }
    
    /**
     * Loads the friends list
     * 
     * @param  file  The file with the data
     * 
     * @throws  IOException  On I/O error
     */
    private static void loadFriends(final String file) throws IOException
    {
	friendFile = file;
	final File $file = new File(file);
	friends = new HashSet<Player>();
	if ($file.exists() == false)
	    saveFriends();
	else
	{
	    ObjectInputStream is = null;
	    Object obj = null;
	    try
	    {
		is = new ObjectInputStream(new BufferedInputStream(new FileInputStream($file)));
		while ((obj = is.readObject()) != null)
		    friends.add((Player)obj);
	    }
	    catch (final ClassNotFoundException err)
	    {
		throw new IOError(err);
	    }
	    catch (final IOException | RuntimeException | Error err)
	    {
		System.err.println(err);
		System.err.println("obj: " + obj);
		System.err.println("is: " + is);
		throw err;
	    }
	    finally
	    {
		if (is != null)
		    try
		    {
			is.close();
		    }
		    catch (final Throwable err)
		    {
			//Do nothing
		    }
	    }
	}
    }
    
    
    /**
     * Returns the list of friends
     * 
     * @return  The list of friends
     */
    public static Player[] getFriends()
    {
	synchronized (monitor)
	{
	    final Player[] rc = new Player[friends.size()];
	    friends.toArray(rc);
	    return rc;
	}
    }
    
    
    /**
     * Updates the information in the friend list about a player if that player is a friend
     * 
     * @param  friend  The player
     */
    public static void updateFriend(final Player friend)
    {
	synchronized (monitor)
	{
	    if (friends.contains(friend) == false)
		return;
		
	    saveFriends();
	}
    }
    
    
    /**
     * Adds a player to the friend list
     * 
     * @param  friend  The player
     */
    public static void addFriend(final Player friend)
    {
	synchronized (monitor)
	{
	    if (friends.contains(friend))
		return;
	    
	    friends.add(friend);
	    saveFriends();
	}
    }
    
    
    /**
     * Removes a player from the friend list
     * 
     * @param  friend  The player
     */
    public static void removeFriend(final Player friend)
    {
	synchronized (monitor)
	{
	    if (friends.contains(friend) == false)
		return;
	    
	    friends.remove(friend);
	    saveFriends();
	}
    }
    
    
    /**
     * Updates the file with the friend information
     */
    private static void saveFriends()
    {
	synchronized (monitor)
	{
	    System.err.println("saving: " + friends);
	    ObjectOutputStream os = null;
	    try
	    {
		os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(friendFile))));
		for (final Player friend : friends)
		    os.writeObject(friend);
		os.writeObject(null);
		os.flush();
	    }
	    catch (final IOException err)
	    {
		System.err.println("Cannot save local user data: " + err.toString());
	    }
	    finally
	    {
		if (os != null)
		    try
		    {
			os.close();
		    }
		    catch (final Throwable err)
		    {
			//Do nothing
		    }
	    }
	}
    }
    
    
    /**
     * Load, creates if missing, information about the local users
     * 
     * @param  file  The file with the data
     * 
     * @throws  IOException  On I/O error
     */
    private static void loadMe(final String file) throws IOException
    {
	myFile = file;
	final File $file = new File(file);
	if ($file.exists() == false)
	{
	    myUUID = UUID.randomUUID();
	    updateMe(null);
	}
	else
	{
	    ObjectInputStream is = null;
	    try
	    {
		is = new ObjectInputStream(new BufferedInputStream(new FileInputStream($file)));
		Object obj;
		for (;;)
		    if ((obj = is.readObject()) instanceof UUID)
		    {
			System.err.println("Loading UUID: " + obj);
			myUUID = (UUID)obj;
			break;
		    }
		    else
		    {
			System.err.println("Loading DNS: " + obj);
			myDNSes.add((String)obj);
		    }
	    }
	    catch (final ClassNotFoundException err)
	    {
		throw new IOError(err);
	    }
	    finally
	    {
		if (is != null)
		    try
		    {
			is.close();
		    }
		    catch (final Throwable err)
		    {
			//Do nothing
		    }
	    }
	}
    }
    
    
    /**
     * Updates the file with the local user's information
     * 
     * @param  me  The player
     */
    public static void updateMe(final Player me)
    {
	synchronized (monitor)
	{
	    if (me != null)
		myDNSes = me.getDNSes();
	    ObjectOutputStream os = null;
	    try
	    {
		os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(myFile))));
		for (final String dns : myDNSes)
		{
		    System.err.println("Saving DNS: " + dns);
		    os.writeObject(dns);
		}
		System.err.println("Saving UUID: " + myUUID);
		os.writeObject(myUUID);
		os.flush();
	    }
	    catch (final IOException err)
	    {
		System.err.println("Cannot save local user data: " + err.toString());
	    }
	    finally
	    {
		if (os != null)
		    try
		    {
			os.close();
		    }
		    catch (final Throwable err)
		    {
			//Do nothing
		    }
	    }
	}
    }
    
    
    /**
     * Gets the local user's DNS names
     * 
     * @return  The local user's DNS names
     */
    public static String[] getPersonalDNSes()
    {
	synchronized (monitor)
	{
	    final String[] rc = new String[myDNSes.size()];
	    myDNSes.toArray(rc);
	    return rc;
	}
    }
    
    
    /**
     * Gets the local user's UUID
     * 
     * @retun  The local user's UUID
     */
    public static UUID getPersonalUUID()
    {
	return myUUID;
    }
    
}
