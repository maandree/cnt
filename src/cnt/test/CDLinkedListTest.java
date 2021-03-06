/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.test;
import cnt.util.*;

import java.util.Iterator;


/**
 * Test class for {@link CDLinkedList}
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class CDLinkedListTest
{
    /**
     * Non-constructor
     */
    private CDLinkedListTest()
    {
	assert false : "You may not create instances of this class [CDLinkedListTest].";
    }
    
    
    
    /**
     * The is the main entry point of the test
     * 
     * @param  args  Starup arguments, unused
     */
    public static void main(final String... args)
    {
	final CDLinkedList<String> list = new CDLinkedList<String>();
	
	
	assertion("Empty test", list.isEmpty());
	
	System.out.println("Forward rotating empty list");
	list.next();
	
	System.out.println("Backward rotating empty list");
	list.previous();
	
	assertion("Empty test", list.isEmpty());
	
	System.out.println("Adding first item");
	list.insertAfter("1");
	
	assertion("Getting current item", list.get(), "1");
	assertion("Getting previous item", list.getPrevious(), "1");
	assertion("Getting next item", list.getNext(), "1");
	
	System.out.println("Forward rotating");
	list.next();
	assertion("Getting current item", list.get(), "1");

	System.out.println("Backward rotating");
	list.previous();
	assertion("Getting current item", list.get(), "1");
	
	
	assertion("Non-empty test", list.isEmpty() == false);
	
	System.out.println("Adding item after current");
	list.insertAfter("2");
	
	assertion("Non-empty test", list.isEmpty() == false);
	
	System.out.println("Adding item before current");
	list.insertBefore("0");
	
	assertion("Getting current item", list.get(), "1");
	assertion("Getting previous item", list.getPrevious(), "0");
	assertion("Getting next item", list.getNext(), "2");
	
	System.out.println("Forward rotating");
	list.next();
	
	assertion("Getting current item", list.get(), "2");
	assertion("Getting previous item", list.getPrevious(), "1");
	assertion("Getting next item", list.getNext(), "0");
	
	System.out.println("Forward rotating");
	list.next();
	
	assertion("Getting current item", list.get(), "0");
	assertion("Getting previous item", list.getPrevious(), "2");
	assertion("Getting next item", list.getNext(), "1");
	
	System.out.println("Forward rotating");
	list.next();
	
	assertion("Getting current item", list.get(), "1");
	assertion("Getting previous item", list.getPrevious(), "0");
	assertion("Getting next item", list.getNext(), "2");
	
	System.out.println("Backward rotating");
	list.previous();
	
	assertion("Getting current item", list.get(), "0");
	assertion("Getting previous item", list.getPrevious(), "2");
	assertion("Getting next item", list.getNext(), "1");
	
	final String[] items = {"0", "1", "2", };
	
	int i = 0;
	for (final String item : list)
	    assertion("Testing iteration", item, items[i++]);
	assertion("Iterated correctly", i == 3);
	
	i = 0;
	for (final String item : list)
	    assertion("Testing iteration", item, items[i++]);
	assertion("Iterated correctly", i == 3);
	
	assertion("Getting current item", list.get(), "0");
	
	Iterator<String> iter = list.iterator();
	for (i = 0; i < 10; i++)
	    assertion("Testing iteration", iter.next(), items[i % 3]);
	
	System.out.println("Searching and removing");
	list.remove(list.find("0"));
	
	assertion("Getting current item", list.get(), "2");
	assertion("Getting previous item", list.getPrevious(), "1");
	assertion("Getting next item", list.getNext(), "1");
	
	iter = list.iterator();
	System.out.println("Removing using iterator");
	iter.remove();
	assertion("Getting current item", list.get(), "1");
	assertion("Getting previous item", list.getPrevious(), "1");
	assertion("Getting next item", list.getNext(), "1");
	
	System.out.println("Emptying list");
	list.clear();
	
	assertion("Empty test", list.isEmpty());
	
	
	System.out.println("\nPassed all!");
    }
    
    
    
    /**
     * Prints a description of a test and whether it fail or passed.<br/>
     * If the test, which is perform by this method, failed the program exits with a user error code.
     * 
     * @param  description  The description of the test, one line
     * @param  got          The value the test rutine got
     * @param  expected     The expected value
     */
    private static void assertion(final String description, final Object got, final Object expected)
    {
	System.out.println(description + ": " + (got.equals(expected) ? "passed"
		: ("failed: got " + got.toString() + " expected " + expected.toString())));
	if (got.equals(expected) == false)
	    System.exit(-1);
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
