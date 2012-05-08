package cnt.test;
import cnt.game.*;

public class shapeTest
{
	public static void main(String... args) throws InterruptedException
	{
		System.out.println("Setting up");
		Player player = new Player("CNT", 3421236);
		System.out.println("Testing to create an IShape");
		Shape ishape = new IShape(player);
		System.out.println(ishape);
		System.out.println("Testing rotateing IShape");
		ishape.rotate(true);
		System.out.println(ishape);
		System.out.println("Done");
		Thread.sleep(1000);
		
		System.out.println("Testing to create an LShape");
		Shape lshape = new LShape(player);
		System.out.println(lshape);
		System.out.println("Testing rotateing LShape");
		lshape.rotate(true);
		System.out.println(lshape);
		System.out.println("Done");
		Thread.sleep(1000);

		System.out.println("Testing to create an JShape");
		Shape jshape = new JShape(player);
		System.out.println(jshape);
		System.out.println("Testing rotateing JShape");
		jshape.rotate(true);
		System.out.println(jshape);
		System.out.println("Done");
		Thread.sleep(1000);		

		System.out.println("Testing to create an SShape");
		Shape sshape = new SShape(player);
		System.out.println(sshape);
		System.out.println("Testing rotateing SShape");
		sshape.rotate(true);
		System.out.println(sshape);
		System.out.println("Done");
		Thread.sleep(1000);

		System.out.println("Testing to create an ZShape");
		Shape zshape = new ZShape(player);
		System.out.println(zshape);
		System.out.println("Testing rotateing zShape");
		zshape.rotate(true);
		System.out.println(zshape);
		System.out.println("Done");
		Thread.sleep(1000);

		System.out.println("Testing to create an TShape");
		Shape tshape = new TShape(player);
		System.out.println(tshape);
		System.out.println("Testing rotateing TShape");
		tshape.rotate(true);
		System.out.println(tshape);
		System.out.println("Done");
		Thread.sleep(1000);

		System.out.println("Testing to create an OShape");
		Shape oshape = new OShape(player);
		System.out.println(oshape);
		System.out.println("Testing rotateing OShape");
		oshape.rotate(true);
		System.out.println(oshape);
		System.out.println("Done");
		Thread.sleep(1000);
	}
}
