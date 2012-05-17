/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.test;
import cnt.util.*;


/**
 * Test class for {@link ACDLinkedList}
 * 
 * @author  Peyman Eshtiagh
 */
public class ACDLinkedListTest
{
    /**
     * Non-constructor
     */
    private ACDLinkedListTest()
    {
	assert false : "You may not create instances of this class [ACDLinkedListTest].";
    }
    
    
    
    /**
     * This is main entry point of the test
     * 
     * @param  args  Startup arguments, unused
     */
    public static void main(final String... args)
    {
	ACDLinkedList<String> linkedList = new ACDLinkedList<String>();
	
	String P = "Peyman";
	String C = "Calle";
	String M = "Matte";
	String F = "Test";
	
	System.out.println("Adding Peyman");
	linkedList.insertAfter(P);
	System.out.println("Adding Calle");
	linkedList.insertAfter(C);
	System.out.println("Adding Mattias");
	linkedList.insertAfter(M);
	
	assertion("Testing existance of Peyman", linkedList.contains(P));
	assertion("Testing existance of Calle", linkedList.contains(C));
	assertion("Testing existance of Matte", linkedList.contains(M));
	
	System.out.println("Searching for and removing Matte");
	linkedList.remove(linkedList.find("Matte"));
	assertion("Testing non-existance of Matte", linkedList.contains(M) == false);
	
        System.out.println("Adding another");
	linkedList.insertBefore(F);

        System.out.println("\nPassed all!");
    }
    
    
    
    /**
     * Prints a description of a test and whether it fail or passed.<br/>
     * If the test failed the program exits with a user error code.
     * 
     * @param  description  The description of the test, one line
     * @param  passed       Whether the test passed
     */
    private static void assertion(final String description, final boolean passed)
    {
        System.out.println(description + ": " + (passed ? "passed" : "failed"));
        if (passed == false)
            System.exit(-1);
    }

}

