interface NetworkMessage
{
	/**
	* String representation of the message
	*/
	String messageText = null;

	/**
	* Get human readable version of message
	*/
        public String getMessageText()
    {
	return this.messageText;
    }
}
