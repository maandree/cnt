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
	String dir = System.getProperty("user.home");
	final String dirsep = System.getProperty("file.separator");
	
	if (dir.endsWith(dirsep) == false)
	    dir += dirsep + ".cnt" + dirsep;
	else
	    dir += ".cnt" + dirsep;
	
	loadFriends(dir + "friends");
	loadMe(dir + "local");
    }
    
    
    
    /**
     * Load, creates if missing, information about the local users
     * 
     * @param  file  The file with the data
     */
    private static void loadMe(final String file)
    {
    }
    
    
    /**
     * Loads the friends list
     * 
     * @param  file  The file with the data
     */
    private static void loadFriends(final String file)
    {
    }
    
    
    /**
     * Returns the list of friends
     * 
     * @return  The list of friends
     */
    public static Player[] getFriends()
    {
	return null;
    }
    
    
    /**
     * Updates the information in the friend list about a player if that player is a friend
     * 
     * @param  friend  The player
     */
    public static void updateFriend(final Player friend)
    {
    }
    
    
    /**
     * Adds a player to the friend list
     * 
     * @param  friend  The player
     */
    public static void addFriend(final Player friend)
    {
    }
    
    
    /**
     * Removes a player from the friend list
     * 
     * @param  friend  The player
     */
    public static void removeFriend(final Player friend)
    {
    }
    
    
    /**
     * Updates the file with the local user's information
     */
    public static void updateMe()
    {
    }
    
    
    /**
     * Gets the local user's DNS names
     * 
     * @return  The local user's DNS names
     */
    public static String[] getPersonalDNSes()
    {
	return null;
    }
    
    
    /**
     * Gets the local user's UUID
     * 
     * @retun  The local user's UUID
     */
    public static UUID getPersonalUUID()
    {
	return null;
    }
    
}
