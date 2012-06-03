/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.game;


/**
 * Shape class representing a I-shape
 * 
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class IShape extends Shape
{
    /**
     * Compatibility versioning for {@link java.io.Serializable}
     */
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * How much to change {@link #x} by at each 90° clockwise rotation, depending rotation count
     */
    private static final int[] xrot = {-1,  2, -2,  1};
    
    /**
     * How much to change {@link #y} by at each 90° clockwise rotation, depending rotation count
     */
    private static final int[] yrot = { 1, -1,  2, -2};
    
    /*                         *\
      .x..   ....   ..x.   ....
      .x..   xxxx   ..x.   ....
      .x..   ....   ..x.   xxxx
      .x..   ....   ..x.   ....
    \*                         */
    
    
    
    /**
     * Constructor
     */
    public IShape()
    {
	this.shape = new Block[][]
	        {
		    {new Block()},
		    {new Block()},
		    {new Block()},
		    {new Block()},
		};
	
	this.states = new Block[][][]
	        {
		    this.shape,
		    { {new Block(), new Block(), new Block(), new Block()}, }
		};
    }
    
    /**
     * Cloning constructor
     * 
     * @param  original  The shape to clone
     */
    private IShape(final IShape original)
    {
	original.cloneData(this);
	this.currState = original.currState;
	this.crot = original.crot;
	
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
     * The number of clockwise rotations made, minus the number
     * of anti-clockwise rotations make; modulo 4.
     */
    int crot = 0;
    
    
    
    /**
     * Momento class for {@link IShape}
     */
    public static class Momento extends Shape.Momento
    {
	/**
	 * Compatibility versioning for {@link java.io.Serializable}
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Constructor
	 * 
	 * @param  shape  The shape of which to save the state
	 */
        public Momento(final IShape shape)
        {
            super(shape);
            this.currState = shape.currState;
            this.crot = shape.crot;
        }
        
	
	
	/**
	 * See {@link IShape#currState}
	 */
	private final int currState;
	
	/**
	 * See {@link IShape#crot}
	 */
	private final int crot;
	
	
	
        /**
         * Restores the shape's state
         * 
         * @param  Shape  The shape
         */
        public void restore(final Shape shape)
        {
            if (shape instanceof IShape == false)
                throw new Error("Wrong shape type: you have " + shape.getClass().toString());
            super.restore(shape);
            ((IShape)shape).currState = this.currState;
            ((IShape)shape).crot = this.crot;
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
    public IShape clone() {
	return new IShape(this);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void rotate(final boolean clockwise)
    {
	if (clockwise)
	{
	    super.x += xrot[this.crot];
	    super.y += yrot[this.crot];
	    this.crot = (this.crot + 1) % 4;
	}
	
	this.currState = 1 - this.currState;
	this.shape = this.states[this.currState];
	
	if (clockwise == false)
	{
	    this.crot--;
	    if (this.crot < 0)
		this.crot += 4;
	    super.x -= xrot[this.crot];
	    super.y -= yrot[this.crot];
	}
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
