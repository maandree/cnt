/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game.enginehelp;
import cnt.game.*;
import cnt.messages.*;
import cnt.*;

import java.lang.ref.*;
import java.util.*;


/**
 * Game engine data class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class EngineData
{
    //Has default constructor
    
    
    
    /**
     * The current board with all stationed blocks
     */
    public Board board = null;
    
    /**
     * The current falling shape
     */
    public Shape fallingShape = null;
    
    /**
     * The current player
     */
    public Player currentPlayer = null;
    
    /**
     * The local player
     */
    public transient Player localPlayer = null;
    
    /**
     * The momento of the falling shape at the beginning of the move
     */
    public Shape.Momento moveInitialMomento = null;
    
    /**
     * The momento of the falling shape at the end of the move
     */
    public Shape.Momento moveAppliedMomento = null;
    
    /**
     * The interval between falls
     */
    public double sleepTime = 1000;
    
    /**
     * The game thread
     */
    public transient Thread thread = null;
    
    /**
     * Whether the game is over
     */
    public boolean gameOver = true;
    
    /**
     * Shape for shapes with set player
     */
    public transient final HashMap<Player, HashMap<Shape, SoftReference<Shape>>> shapeCache = new HashMap<Player, HashMap<Shape, SoftReference<Shape>>>();
    
    /**
     * The current game score
     */
    public int score;
    
    /**
     * The next score at which to slow down the speed
     */
    public int slowDownScore = 1000;
    
    /**
     * Whether the current player is in pause mode
     */
    public transient boolean paused = false;
    
    /**
     * Pause monitor
     */
    public transient final Object pauseMonitor = new Object();
    
    /**
     * Whether the game is in emergency pause mode
     */
    public transient boolean empaused = false;
    
    /**
     * Emergancy pause monitor
     */
    public transient final Object empauseMonitor = new Object();
    
    /**
     * Matrix patcher
     */
    public transient final Patcher patcher = new Patcher();
    
}

