/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt;

import java.io.*;


/**
 * Game session recorder
 *
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class Recorder implements Blackboard.BlackboardObserver
{
    /**
     * Constructor
     * 
     * @param  tape  The files to which to record the game session
     */
    public Recorder(final String tape)
    {
	ObjectOutputStream _tape = null;
	try
	{
	    _tape = new ObjectOutputStream(
		      new BufferedOutputStream(
			new FileOutputStream(
			  new File(tape)
		    ) ) );
	}
	catch (final Throwable err)
	{
	    System.err.println("\033[1;31mWe are experiencing some problems recording to your file.\033[21;39m");
	}
	
	this.tape = _tape;
    }
    
    
    /**
     * The files to which to record the game session
     */
    private final ObjectOutputStream tape;
    
    /**
     * The time, in milli seconds the last message was received
     */
    private long lastTime = 0;
    
    
    
    /**
     * Starts the recording
     */
    public void start()
    {
	if (tape != null)
	    Blackboard.registerObserver(this);
	this.lastTime = System.currentTimeMillis();
    }
    
    /**
     * {@inheritDoc}
     */
    public void messageBroadcasted(final Blackboard.BlackboardMessage message)
    {
	try
	{
	    final long now = System.currentTimeMillis();
	    this.tape.writeInt((int)(now - this.lastTime));
	    this.tape.writeObject(message);
	    this.tape.flush();
	    this.lastTime = now;
	}
	catch (final Throwable err)
	{
	    Blackboard.unregisterObserver(this);
	    System.err.println("\033[1;31mWe are experiencing some problems recording to your file.\033[21;39m");
	}
    }
    
    /**
     * Stops the recording
     */
    public void stop()
    {
	Blackboard.unregisterObserver(this);
	if (this.tape != null)
	    try
	    {
		tape.flush();
		tape.close();
	    }
	    catch (final Throwable err)
	    {
		System.err.println("We are experiencing some problems closing your recording file.");
	    }
    }
    
}

