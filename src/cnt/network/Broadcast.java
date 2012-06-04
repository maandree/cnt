import cnt.Blackboard;

public class Broadcast implements NetworkMessage
{

	/**
	* Constructor encaupsuling a BlackboardMessage as a broadcast to all clients
	*
	* @param from ID of sender
	* @param message the BlackboardMessage to send
	*/
	public Handshake(int from, BlackboradMessage message)
	{
		this.from = from;
		this.message = message;
		this.messageTxt = message.toString();
	}

	protected final int from;
	protected final BlackboardMessage message;
	public final String messageTxt;
	
	public String getMessageTxt()
	{
		return this.messageTxt;
	}

	public int getSender()
	{
		return this.from;
	}

	public BlackboardMessage getMessage()
	{
		return this.message;
	}

}
