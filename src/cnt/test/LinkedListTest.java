/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright Ⓒ 2012  Mattias Andrée, Peyman Eshtiagh,
 *                   Calle Lejdbrandt, Magnus Lundberg
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.test;
import cnt.util.*;

import java.util.Iterator;


/**
 * Linked list classes test
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@kth.se">maandree@kth.se</a>
 */
public class LinkedListTest
{
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
    
    public static void assertion(final String description, final Object a, final Object b)
    {
	System.out.println(description + ": " + (a.equals(b) ? "passed" : ("failed: got " + b.toString() + " expected " + a.toString())));
	if (a.equals(b) == false)
	    System.exit(-1);
    }
    
    public static void assertion(final String description, final boolean passed)
    {
	System.out.println(description + ": " + (passed ? "passed" : "failed"));
	if (passed == false)
	    System.exit(-1);
    }
    
}

