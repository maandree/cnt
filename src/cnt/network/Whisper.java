import cnt.Blackboard;

public class Whisper implements NetworkMessage
{

	/**
	* Constructor encaupsuling a BlackboardMessage as a whisper to a specefic client
	*
	* @param from ID of sender
	* @param to Id of reciver
	* @param message the BlackboardMessage to send
	*/
	public Handshake(int from, int to, BlackboradMessage message)
	{
		this.from = from;
		this.to = to;
		this.message = message;
		this.messageTxt = message.toString();
	}

	protected final int from, to;
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

	public int getReciver()
	{
		return this.to;
	}

	public BlackboardMessage getMessage()
	{
		return this.message;
	}

}
