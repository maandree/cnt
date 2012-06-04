/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

public class Handshake extends NetworkMessage
{
    /**
     * Compatibility versioning for {@link Serializable}
     */
    private static final long serialVersionUID = 1L;

    
    
	/**
	* Constructor taking an ID as param, ID &lt; 0 is asking for a new id
	*
	* @param id the player id, &lt; 0 if asking for new
	*/
	public Handshake(int id)
	{
		this.id = id;
		
		if (id < 0)
			this.messageTxt = "Asking for new ID";
		else
			this.messageTxt = "Telling remote host my ID";
	}

    
    
	public final String messageTxt;

    
    
	public String getMessageTxt()
	{
		return this.messageTxt;
	}

}
