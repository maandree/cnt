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
    
    
    
    static
    {
	loadFriends();
    }
    
    
    
    private static void loadFriends()
    {
    }
    
    public static Player[] getFriends()
    {
	return null;
    }
    
    public static void updateFriend(final Player friend)
    {
    }
    
    public static void addFriend(final Player friend)
    {
    }
    
    public static void removeFriend(final Player friend)
    {
    }
    
    public static UUID getPersonalUUID()
    {
	return null;
    }
    
}
