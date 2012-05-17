/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.test;
import cnt.game.*;


/**
 * Tests to create shapes and rotate 90° clockwise
 * 
 * @author  Calle Lejdbrandt, <a href="mailto:callel@kth.se">callel@kth.se</a>
 */
public class ShapeTest
{
    /**
     * Non-constructor
     */
    private ShapeTest()
    {
	assert false : "You may not create instances of this class [ShapeTest].";
    }
    
    
    
    /**
     * This is the main entry point of the test
     * 
     * @param  args  Startup arguments, unused
     * 
     * @throws  InterruptedException  Will only be throws if you start this from another method and interrupts the thread
     */
    public static void main(String... args) throws InterruptedException
    {
	System.out.println("Testing to create an IShape");
	Shape ishape = new IShape();
	System.out.println(ishape);
	System.out.println("Testing rotating IShape");
	ishape.rotate(true);
	System.out.println(ishape);
	System.out.println("Done");
	Thread.sleep(1000);
		
	System.out.println("Testing to create an LShape");
	Shape lshape = new LShape();
	System.out.println(lshape);
	System.out.println("Testing rotating LShape");
	lshape.rotate(true);
	System.out.println(lshape);
	System.out.println("Done");
	Thread.sleep(1000);

	System.out.println("Testing to create an JShape");
	Shape jshape = new JShape();
	System.out.println(jshape);
	System.out.println("Testing rotating JShape");
	jshape.rotate(true);
	System.out.println(jshape);
	System.out.println("Done");
	Thread.sleep(1000);		

	System.out.println("Testing to create an SShape");
	Shape sshape = new SShape();
	System.out.println(sshape);
	System.out.println("Testing rotating SShape");
	sshape.rotate(true);
	System.out.println(sshape);
	System.out.println("Done");
	Thread.sleep(1000);

	System.out.println("Testing to create an ZShape");
	Shape zshape = new ZShape();
	System.out.println(zshape);
	System.out.println("Testing rotating ZShape");
	zshape.rotate(true);
	System.out.println(zshape);
	System.out.println("Done");
	Thread.sleep(1000);

	System.out.println("Testing to create an TShape");
	Shape tshape = new TShape();
	System.out.println(tshape);
	System.out.println("Testing rotating TShape");
	tshape.rotate(true);
	System.out.println(tshape);
	System.out.println("Done");
	Thread.sleep(1000);

	System.out.println("Testing to create an OShape");
	Shape oshape = new OShape();
	System.out.println(oshape);
	System.out.println("Testing rotating OShape");
	oshape.rotate(true);
	System.out.println(oshape);
	System.out.println("Done");
	Thread.sleep(1000);
    }
}
