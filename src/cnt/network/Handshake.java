/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;


public class Handshake implements ConnectionMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;

    
	/**
	* Constructor taking an ID as param, ID &lt; 0 is asking for a new id
	*/
	public Handshake()
	{
	    this(-1);
	}
    
	/**
	* Constructor taking an ID as param, ID &lt; 0 is asking for a new id
	*
	* @param id the player id, &lt; 0 if asking for new
	*/
	public Handshake(int id)
	{
		this.id = id;
		
		if (id < 0)
			this.messageText = "Asking for new ID";
		else
			this.messageText = "Telling remote host my ID";
	}

    
    
    final int id;
    
	public final String messageText;

    
    
    public String toString()
	{
		return this.messageText;
	}

    public int getID()
    {
	    return this.id;
    }

}
