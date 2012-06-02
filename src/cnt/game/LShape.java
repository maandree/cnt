/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;


/**
 * Shape class representing a L-shape
 * 
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class LShape extends Shape
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;	
    
    
    
    /**
     * How much to change {@link #x} by at each 90° clockwise rotation, depending on the state
     */
    private static final int[] xrot = {0, 1, -1, 0};
    
    /**
     * How much to change {@link #y} by at each 90° clockwise rotation, depending on the state
     */
    private static final int[] yrot = {0, 0,  1, -1};
    
    
    
    /**
     * Constructor
     */
    public LShape()
    {
	this.shape = new Block[][]
	        {
		    {new Block(), null       },
		    {new Block(), null       },
		    {new Block(), new Block()},
		};
	
	this.states = new Block[][][]
	        {
		    this.shape,
		    
		    {  {new Block(), new Block(), new Block()},
		       {new Block(), null,        null       },  },
		    
		    {  {new Block(), new Block()},
		       {null,        new Block()},
		       {null,        new Block()},  },
		       
		    {  {null,        null     ,   new Block()},
		       {new Block(), new Block(), new Block()},  },
		};
    }
    
    /**
     * Cloning constructor
     * 
     * @param  original  The shape to clone
     */
    private LShape(final LShape original)
    {
	original.cloneData(this);
	this.currState = original.currState;
	
	int d, w, h;
	this.states = new Block[d = original.states.length][][];
	
	for (int z = 0; z < d; z++)
	{
	    Block[][] os = original.states[z];
	    Block[][] s = this.states[z] =
		    new Block[h = os.length]
		             [w = os[0].length];
	    
	    for (int y = 0; y < h; y++)
	    {
		Block[] row = s[y];
		Block[] orow = os[y];
		for (int x = 0; x < w; x++)
		    if (orow[x] != null)
			row[x] = new Block(orow[x].getColor());
	    }
	}
    }
    
    
    
    /**
     * The shape's possible states
     */
    Block[][][] states;
    
    /**
     * The index of the shape's current state
     */
    int currState = 0;
    
    
    
    /**
     * Momento class for {@link LShape}
     */
    public static class Momento extends Shape.Momento
    {
	/**
	 * Constructor
	 * 
	 * @param  shape  The shape of which to save the state
	 */
        public Momento(final LShape shape)
        {
            super(shape);
            this.currState = shape.currState;
        }
	
	
	
	/**
	 * See {@link LShape#currState}
	 */
	private final int currState;
	
	
	
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof LShape == false)
                throw new Error("Wrong shape type: you have " + shape.getClass().toString());
            super.restore(shape);
            ((LShape)shape).currState = this.currState;
        }
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    public Momento store() {
        return new Momento(this);
    }
    
    /**
     * {@inheritDoc}
     */	
    public LShape clone() {
	return new LShape(this);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void rotate(final boolean clockwise)
    {
	if (clockwise)
	{
	    super.x += xrot[this.currState];
	    super.y += yrot[this.currState];
	}
	
	if (clockwise)
	    this.currState = (this.currState + 1) % 4;
	else
	    this.currState = (this.currState - 1) < 0
		             ? (this.currState + 3)
		             : (this.currState - 1);
	
	if (clockwise == false)
	{
	    super.x -= xrot[this.currState];
	    super.y -= yrot[this.currState];
	}
	
	this.shape = this.states[this.currState];
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayer(final Player value)
    {
	super.setPlayer(value);
	for (final Block[][] state : states)
	    for (final Block[] row : state)
		for (final Block block : row)
		    if (block != null)
			block.setColor(value.getID());
    }
}
