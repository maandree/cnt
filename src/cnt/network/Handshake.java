/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
public class Handshake extends NetworkMessage
{

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
